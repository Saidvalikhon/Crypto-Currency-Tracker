package com.example.cryptocurrencytracker.ui.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytracker.domain.model.AppError
import com.example.cryptocurrencytracker.domain.model.CoinDetails
import com.example.cryptocurrencytracker.domain.repository.CoinRepository
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope


private const val USE_SUPERVISOR_SCOPE = true

private const val DEBUG_EXPERIMENT = false

class CoinDetailsViewModel(
    private val coinId: String,
    private val repository: CoinRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoinDetailsUiState())
    val uiState: StateFlow<CoinDetailsUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CoinDetailsEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val events: SharedFlow<CoinDetailsEvent> = _events.asSharedFlow()

    private var loadJob: Job? = null

    init {
        observeCoin()
        load()
    }

    fun onPeriodSelected(period: ChartPeriod) {
        _uiState.update { it.copy(period = period) }
    }

    fun onFavoriteClick() {
        val coin = _uiState.value.coin ?: return
        viewModelScope.launch {
            try {
                repository.setFavorite(coin.id, !coin.isFavorite)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _events.tryEmit(CoinDetailsEvent.ShowError(e.asAppError()))
            }
        }
    }

    private fun observeCoin() {
        val coinFlow = repository.observeCoinWithHistory(coinId)
            .catch { e ->
                reportError(e.asAppError())
                emit(null)
            }

        val favoritesFlow = repository.observeFavoriteIds()
            .catch { e ->
                reportError(e.asAppError())
                emit(emptySet())
            }

        viewModelScope.launch {
            combine(coinFlow, favoritesFlow) { data, favoriteIds ->
                data?.copy(coin = data.coin.copy(isFavorite = data.coin.id in favoriteIds))
            }.collect { data ->
                _uiState.update { state ->
                    state.copy(
                        coin = data?.coin,
                        history = data?.history.orEmpty(),
                        hasLoadedOnce = true
                    )
                }
            }
        }
    }

    fun load() {
        if (loadJob?.isActive == true) return

        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                if (USE_SUPERVISOR_SCOPE) loadIndependent() else loadRequired()
            } catch (e: CancellationException) {
                throw e
            } catch (e: AppError) {
                reportLoadError(e)
            } catch (e: Exception) {
                reportLoadError(AppError.Unknown(e))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun loadRequired() = coroutineScope {
        val detailsDeferred = async { fetchDetailsStep() }
        val historyDeferred = async { refreshHistoryStep() }

        val details = detailsDeferred.await()
        historyDeferred.await()

        _uiState.update { it.copy(details = details) }
    }

    private suspend fun loadIndependent() = supervisorScope {
        val detailsDeferred = async { fetchDetailsStep() }
        val historyDeferred = async { refreshHistoryStep() }

        var firstError: AppError? = null

        try {
            val details = detailsDeferred.await()
            _uiState.update { it.copy(details = details) }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            firstError = e.asAppError()
        }

        try {
            historyDeferred.await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            if (firstError == null) firstError = e.asAppError()
        }

        firstError?.let { throw it }
    }

    private suspend fun fetchDetailsStep(): CoinDetails {
        if (DEBUG_EXPERIMENT) {
            delay(100)
            Log.d("Day14", "details: падаю искусственно")
            throw AppError.Network()
        }
        return repository.fetchDetails(coinId)
    }

    private suspend fun refreshHistoryStep() {
        try {
            if (DEBUG_EXPERIMENT) delay(1500)
            repository.refreshHistory(coinId)
            Log.d("Day14", "history: завершилась")
        } catch (e: CancellationException) {
            Log.d("Day14", "history: ОТМЕНЕНА")
            throw e
        }
    }

    private suspend fun reportLoadError(error: AppError) {
        _uiState.first { it.hasLoadedOnce }
        reportError(error)
    }

    private fun reportError(error: AppError) {
        if (_uiState.value.coin != null) {
            _events.tryEmit(CoinDetailsEvent.ShowError(error))
        } else {
            _uiState.update { it.copy(error = error) }
        }
    }

    private fun Throwable.asAppError(): AppError = this as? AppError ?: AppError.Unknown(this)
}