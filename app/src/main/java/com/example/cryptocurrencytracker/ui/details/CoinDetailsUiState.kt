package com.example.cryptocurrencytracker.ui.details

import com.example.cryptocurrencytracker.domain.model.AppError
import com.example.cryptocurrencytracker.domain.model.Coin
import com.example.cryptocurrencytracker.domain.model.CoinDetails
import com.example.cryptocurrencytracker.domain.model.PricePoint

data class CoinDetailsUiState(
    val coin: Coin? = null,
    val history: List<PricePoint> = emptyList(),
    val period: ChartPeriod = ChartPeriod.DAY,
    val details: CoinDetails? = null,
    val isLoading: Boolean = true,
    val hasLoadedOnce: Boolean = false,
    val error: AppError? = null
) {
    val visiblePoints: List<PricePoint>
        get() {
            val last = history.lastOrNull() ?: return emptyList()
            val cutoff = last.timestamp - period.hours * 3_600_000L
            return history.filter { it.timestamp >= cutoff }
        }
}