package com.example.cryptocurrencytracker.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytracker.domain.model.AppError
import com.example.cryptocurrencytracker.domain.repository.CoinRepository
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val SEARCH_DEBOUNCE_MS = 300L

class CoinListViewModel(
    private val repository: CoinRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoinListUiState())
    val uiState: StateFlow<CoinListUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CoinListEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val events: SharedFlow<CoinListEvent> = _events.asSharedFlow()

    private var refreshJob: Job? = null

    init {
        observeCoins()
        refresh()
    }

    fun onQueryChange(value: String) {
        _uiState.update { it.copy(query = value) }
    }

    fun onFavoriteClick(coinId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                repository.setFavorite(coinId, isFavorite)
            } catch (e: CancellationException) {
                throw e
            } catch (e: AppError) {
                _events.tryEmit(CoinListEvent.ShowError(e))
            } catch (e: Exception) {
                _events.tryEmit(CoinListEvent.ShowError(AppError.Unknown(e)))
            }
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeCoins() {
        val coinsFlow = _uiState
            .map { it.query.trim() }
            .distinctUntilChanged()
            .debounce { query -> if (query.isEmpty()) 0L else SEARCH_DEBOUNCE_MS }
            .flatMapLatest { query ->
                repository.observeCoins(query)
                    .catch { e ->
                        val error = e as? AppError ?: AppError.Unknown(e)
                        _uiState.update { it.copy(hasLoadedOnce = true) }
                        reportError(error)
                    }
            }

        val favoriteIdsFlow = repository.observeFavoriteIds()
            .catch { e ->
                reportError(e as? AppError ?: AppError.Unknown(e))
                emit(emptySet())
            }

        viewModelScope.launch {
            combine(coinsFlow, favoriteIdsFlow) { coins, favoriteIds ->
                coins.map { coin -> coin.copy(isFavorite = coin.id in favoriteIds) }
            }.collect { coins ->
                _uiState.update { state ->
                    state.copy(
                        coins = coins,
                        hasLoadedOnce = true,
                        error = if (coins.isNotEmpty()) null else state.error
                    )
                }
            }
        }
    }

    fun refresh() {
        if (refreshJob?.isActive == true) return

        refreshJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.refresh()
            } catch (e: CancellationException) {
                throw e
            } catch (e: AppError) {
                reportRefreshError(e)
            } catch (e: Exception) {
                reportRefreshError(AppError.Unknown(e))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun reportRefreshError(error: AppError) {
        _uiState.first { it.hasLoadedOnce }
        reportError(error)
    }

    private fun reportError(error: AppError) {
        val state = _uiState.value
        if (state.coins.isNotEmpty() || state.query.isNotBlank()) {
            _events.tryEmit(CoinListEvent.ShowError(error))
        } else {
            _uiState.update { it.copy(error = error) }
        }
    }
}