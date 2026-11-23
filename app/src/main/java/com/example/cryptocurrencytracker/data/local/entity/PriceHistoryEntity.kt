package com.example.cryptocurrencytracker.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "price_history",
    primaryKeys = ["coin_id", "timestamp"],
    indices = [Index(value = ["coin_id"])]
)
data class PriceHistoryEntity(
    @ColumnInfo(name = "coin_id")
    val coinId: String,
    val timestamp: Long,
    val price: Double
)