package com.example.cryptocurrencytracker.ui.list

import com.example.cryptocurrencytracker.domain.model.AppError

sealed interface CoinListEvent {
    data class ShowError(val error: AppError) : CoinListEvent
}