package com.example.cryptocurrencytracker.ui.list

import com.example.cryptocurrencytracker.domain.model.AppError
import com.example.cryptocurrencytracker.domain.model.Coin

data class CoinListUiState(
    val isLoading: Boolean = true,
    val coins: List<Coin> = emptyList(),
    val query: String = "",
    val hasLoadedOnce: Boolean = false,
    val error: AppError? = null,
)