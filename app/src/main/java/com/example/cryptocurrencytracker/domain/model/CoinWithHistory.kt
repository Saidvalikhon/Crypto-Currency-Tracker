package com.example.cryptocurrencytracker.domain.model

data class CoinWithHistory(
    val coin: Coin,
    val history: List<PricePoint>
)