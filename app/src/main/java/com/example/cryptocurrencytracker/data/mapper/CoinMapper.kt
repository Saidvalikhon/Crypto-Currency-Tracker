package com.example.cryptocurrencytracker.data.mapper

import com.example.cryptocurrencytracker.data.remote.dto.CoinDto
import com.example.cryptocurrencytracker.domain.model.Coin

fun CoinDto.toDomain(): Coin? {
    val price = currentPrice ?: return null

    return Coin(
        id = id,
        name = name,
        symbol = symbol,
        imageUrl = image.orEmpty(),
        price = price,
        priceChangeDay = priceChangePercentageDay ?: 0.0,
        marketCapRank = marketCapRank ?: 0
    )
}

fun List<CoinDto>.toDomain(): List<Coin> = mapNotNull { it.toDomain() }