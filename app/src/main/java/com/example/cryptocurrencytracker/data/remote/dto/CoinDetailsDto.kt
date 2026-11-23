package com.example.cryptocurrencytracker.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinDetailsDto(
    val id: String,
    val description: DescriptionDto? = null,
    @SerialName("market_data")
    val marketData: MarketDataDto? = null
)

@Serializable
data class DescriptionDto(
    val en: String? = null
)

@Serializable
data class MarketDataDto(
    @SerialName("market_cap")
    val marketCap: Map<String, Double?> = emptyMap(),
    @SerialName("total_volume")
    val totalVolume: Map<String, Double?> = emptyMap(),
    @SerialName("high_24h")
    val high24h: Map<String, Double?> = emptyMap(),
    @SerialName("low_24h")
    val low24h: Map<String, Double?> = emptyMap()
)