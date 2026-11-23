package com.example.cryptocurrencytracker.data.mapper

import com.example.cryptocurrencytracker.data.remote.dto.CoinDetailsDto
import com.example.cryptocurrencytracker.domain.model.CoinDetails

private const val CURRENCY = "usd"
private val HTML_TAG = Regex("<[^>]*>")

fun CoinDetailsDto.toCoinDetails(): CoinDetails = CoinDetails(
    id = id,
    description = description?.en.orEmpty().replace(HTML_TAG, "").trim(),
    marketCap = marketData?.marketCap?.get(CURRENCY),
    volume24h = marketData?.totalVolume?.get(CURRENCY),
    high24h = marketData?.high24h?.get(CURRENCY),
    low24h = marketData?.low24h?.get(CURRENCY)
)