package com.example.cryptocurrencytracker.data.local

import androidx.room.withTransaction
import com.example.cryptocurrencytracker.data.local.dao.CoinDao
import com.example.cryptocurrencytracker.data.local.dao.FavoriteDao
import com.example.cryptocurrencytracker.data.local.dao.PriceHistoryDao
import com.example.cryptocurrencytracker.data.local.entity.CoinEntity
import com.example.cryptocurrencytracker.data.local.entity.CoinWithHistoryEntity
import com.example.cryptocurrencytracker.data.local.entity.FavoriteEntity
import com.example.cryptocurrencytracker.data.local.entity.PriceHistoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoinLocalDataSource(
    private val db: AppDatabase,
    private val dao: CoinDao,
    private val favoriteDao: FavoriteDao,
    private val priceHistoryDao: PriceHistoryDao
) {
    fun observeCoins(query: String): Flow<List<CoinEntity>> =
        if (query.isBlank()) dao.observeAll() else dao.search(query.toLikePattern())

    fun observeFavoriteIds(): Flow<Set<String>> =
        favoriteDao.observeIds().map { it.toSet() }

    fun observeCoinWithHistory(coinId: String): Flow<CoinWithHistoryEntity?> =
        priceHistoryDao.observeCoinWithHistory(coinId)

    suspend fun replaceAll(coins: List<CoinEntity>) {
        db.withTransaction {
            dao.clear()
            dao.upsertAll(coins)
        }
    }

    suspend fun replaceHistory(coinId: String, points: List<PriceHistoryEntity>) {
        db.withTransaction {
            priceHistoryDao.deleteByCoinId(coinId)
            priceHistoryDao.insertAll(points)
        }
    }

    suspend fun setFavorite(coinId: String, isFavorite: Boolean) {
        if (isFavorite) {
            favoriteDao.insert(FavoriteEntity(coinId))
        } else {
            favoriteDao.delete(coinId)
        }
    }

    private fun String.toLikePattern(): String {
        val escaped = this
            .replace("\\", "\\\\")
            .replace("%", "\\%")
            .replace("_", "\\_")
        return "%$escaped%"
    }
}