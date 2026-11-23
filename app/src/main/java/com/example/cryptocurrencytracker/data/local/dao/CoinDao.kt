package com.example.cryptocurrencytracker.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.cryptocurrencytracker.data.local.entity.CoinEntity
import kotlinx.coroutines.flow.Flow

private const val ORDER_BY_RANK =
    "ORDER BY CASE WHEN market_cap_rank = 0 THEN 1 ELSE 0 END, market_cap_rank ASC"

private const val ORDERED_COINS = "SELECT * FROM coins $ORDER_BY_RANK"

private const val SEARCH_COINS =
    "SELECT * FROM coins " +
            "WHERE name LIKE :pattern ESCAPE '\\' OR symbol LIKE :pattern ESCAPE '\\' " +
            ORDER_BY_RANK

@Dao
interface CoinDao {

    @Upsert
    suspend fun upsertAll(coins: List<CoinEntity>)

    @Query(ORDERED_COINS)
    fun observeAll(): Flow<List<CoinEntity>>

    @Query(SEARCH_COINS)
    fun search(pattern: String): Flow<List<CoinEntity>>

    @Query(ORDERED_COINS)
    suspend fun getAll(): List<CoinEntity>

    @Query("DELETE FROM coins")
    suspend fun clear()

    @Query("SELECT COUNT(*) FROM coins")
    suspend fun count(): Int
}