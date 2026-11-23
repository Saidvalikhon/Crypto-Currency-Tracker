package com.example.cryptocurrencytracker.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MarketChartDto(
    val prices: List<List<Double>> = emptyList()
)