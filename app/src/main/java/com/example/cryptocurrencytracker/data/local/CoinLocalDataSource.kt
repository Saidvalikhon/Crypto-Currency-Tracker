package com.example.cryptocurrencytracker.data.local

import androidx.room.withTransaction
import com.example.cryptocurrencytracker.data.local.dao.CoinDao
import com.example.cryptocurrencytracker.data.local.entity.CoinEntity
import kotlinx.coroutines.flow.Flow

class CoinLocalDataSource(
    private val db: AppDatabase,
    private val dao: CoinDao
) {
    fun observeCoins(): Flow<List<CoinEntity>> = dao.observeAll()

    suspend fun replaceAll(coins: List<CoinEntity>) {
        db.withTransaction {
            dao.clear()
            dao.upsertAll(coins)
        }
    }
}