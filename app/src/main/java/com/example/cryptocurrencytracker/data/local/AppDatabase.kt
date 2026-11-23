package com.example.cryptocurrencytracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cryptocurrencytracker.data.local.dao.CoinDao
import com.example.cryptocurrencytracker.data.local.dao.FavoriteDao
import com.example.cryptocurrencytracker.data.local.dao.PriceHistoryDao
import com.example.cryptocurrencytracker.data.local.entity.CoinEntity
import com.example.cryptocurrencytracker.data.local.entity.FavoriteEntity
import com.example.cryptocurrencytracker.data.local.entity.PriceHistoryEntity

@Database(
    entities = [CoinEntity::class, FavoriteEntity::class, PriceHistoryEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao

    abstract fun favoriteDao(): FavoriteDao

    abstract fun priceHistoryDao(): PriceHistoryDao

    companion object {
        const val NAME = "crypto.db"
    }
}