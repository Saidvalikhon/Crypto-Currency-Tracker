package com.example.cryptocurrencytracker.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinDto(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String? = null,
    @SerialName("current_price")
    val currentPrice: Double? = null,
    @SerialName("price_change_percentage_24h")
    val priceChangePercentageDay: Double? = null,
    @SerialName("market_cap_rank")
    val marketCapRank: Int? = null
)