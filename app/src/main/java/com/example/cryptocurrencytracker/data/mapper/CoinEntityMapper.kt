package com.example.cryptocurrencytracker.data.mapper

import com.example.cryptocurrencytracker.data.local.entity.CoinEntity
import com.example.cryptocurrencytracker.domain.model.Coin

fun CoinEntity.toCoin(): Coin = Coin(
    id = id,
    name = name,
    symbol = symbol,
    imageUrl = imageUrl,
    price = price,
    priceChangeDay = priceChangeDay,
    marketCapRank = marketCapRank
)

fun Coin.toEntity(): CoinEntity = CoinEntity(
    id = id,
    name = name,
    symbol = symbol,
    imageUrl = imageUrl,
    price = price,
    priceChangeDay = priceChangeDay,
    marketCapRank = marketCapRank
)