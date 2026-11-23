package com.example.cryptocurrencytracker.domain.model

data class CoinDetails(
    val id: String,
    val description: String,
    val marketCap: Double?,
    val volume24h: Double?,
    val high24h: Double?,
    val low24h: Double?
)