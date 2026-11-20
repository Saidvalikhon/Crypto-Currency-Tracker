package com.example.cryptocurrencytracker.domain.model

data class Coin(
    val id: String,
    val name: String,
    val symbol: String,
    val imageUrl: String,
    val price: Double,
    val priceChangeDay: Double,
    val marketCapRank: Int
)
