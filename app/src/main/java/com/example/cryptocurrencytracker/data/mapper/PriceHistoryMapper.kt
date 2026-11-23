package com.example.cryptocurrencytracker.data.mapper

import com.example.cryptocurrencytracker.data.local.entity.CoinWithHistoryEntity
import com.example.cryptocurrencytracker.data.local.entity.PriceHistoryEntity
import com.example.cryptocurrencytracker.data.remote.dto.MarketChartDto
import com.example.cryptocurrencytracker.domain.model.CoinWithHistory
import com.example.cryptocurrencytracker.domain.model.PricePoint

fun MarketChartDto.toHistoryEntities(coinId: String): List<PriceHistoryEntity> =
    prices.mapNotNull { pair ->
        if (pair.size < 2) {
            null
        } else {
            PriceHistoryEntity(
                coinId = coinId,
                timestamp = pair[0].toLong(),
                price = pair[1]
            )
        }
    }

fun CoinWithHistoryEntity.toCoinWithHistory(): CoinWithHistory = CoinWithHistory(
    coin = coin.toCoin(),
    history = history
        .sortedBy { it.timestamp }
        .map { PricePoint(timestamp = it.timestamp, price = it.price) }
)