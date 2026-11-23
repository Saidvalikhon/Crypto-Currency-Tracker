package com.example.cryptocurrencytracker.ui.details

import com.example.cryptocurrencytracker.domain.model.AppError

sealed interface CoinDetailsEvent {
    data class ShowError(val error: AppError) : CoinDetailsEvent
}