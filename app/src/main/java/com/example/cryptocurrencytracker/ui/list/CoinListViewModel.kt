package com.example.cryptocurrencytracker.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytracker.domain.model.AppError
import com.example.cryptocurrencytracker.domain.repository.CoinRepository
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoinListViewModel(
    private val repository: CoinRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoinListUiState())
    val uiState: StateFlow<CoinListUiState> = _uiState.asStateFlow()

    private var refreshJob: Job? = null

    init {
        observeCoins()
        refresh()
    }

    private fun observeCoins() {
        viewModelScope.launch {
            repository.observeCoins()
                .catch { e ->
                    val error = e as? AppError ?: AppError.Unknown(e)
                    _uiState.update { it.copy(error = error) }
                }
                .collect { coins ->
                    _uiState.update { it.copy(coins = coins) }
                }
        }
    }

    fun refresh() {
        if (refreshJob?.isActive == true) return

        refreshJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.refresh()
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: AppError) {
                _uiState.update { it.copy(isLoading = false, error = e) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = AppError.Unknown(e)) }
            }
        }
    }
}