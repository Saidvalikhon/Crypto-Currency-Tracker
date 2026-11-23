package com.example.cryptocurrencytracker.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CoinWithHistoryEntity(
    @Embedded
    val coin: CoinEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "coin_id"
    )
    val history: List<PriceHistoryEntity>
)