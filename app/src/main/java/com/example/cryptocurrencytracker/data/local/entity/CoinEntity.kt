package com.example.cryptocurrencytracker.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coins")
data class CoinEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val symbol: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    val price: Double,
    @ColumnInfo(name = "price_change_day")
    val priceChangeDay: Double,
    @ColumnInfo(name = "market_cap_rank")
    val marketCapRank: Int
)
