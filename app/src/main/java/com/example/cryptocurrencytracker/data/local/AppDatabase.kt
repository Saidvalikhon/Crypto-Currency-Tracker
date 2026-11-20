package com.example.cryptocurrencytracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cryptocurrencytracker.data.local.dao.CoinDao
import com.example.cryptocurrencytracker.data.local.entity.CoinEntity

@Database(
    entities = [CoinEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao

    companion object {
        const val NAME = "crypto.db"
    }
}