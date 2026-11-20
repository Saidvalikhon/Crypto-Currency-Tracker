package com.example.cryptocurrencytracker.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytracker.domain.model.AppError
import com.example.cryptocurrencytracker.domain.repository.CoinRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class CoinListViewModel(
    private val repository: CoinRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(CoinListUiState())
    val uiState: StateFlow<CoinListUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadCoins()
    }
    fun loadCoins(){
        if (loadJob?.isActive == true) return

        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val coins = repository.getCoins()
                _uiState.update { it.copy(isLoading = false, coins = coins) }
            }catch (e: CancellationException){
                throw e
            }catch (e: AppError){
                _uiState.update { it.copy(isLoading = false, error = e) }
            }catch (e: Exception){
                _uiState.update { it.copy(isLoading = false, error = AppError.Unknown(e)) }
            }
        }
    }
}