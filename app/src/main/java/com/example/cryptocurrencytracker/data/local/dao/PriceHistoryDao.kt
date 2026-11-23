package com.example.cryptocurrencytracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.cryptocurrencytracker.data.local.entity.CoinWithHistoryEntity
import com.example.cryptocurrencytracker.data.local.entity.PriceHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PriceHistoryDao {
    @Transaction
    @Query("SELECT * FROM coins WHERE id = :coinId")
    fun observeCoinWithHistory(coinId: String): Flow<CoinWithHistoryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(points: List<PriceHistoryEntity>)

    @Query("DELETE FROM price_history WHERE coin_id = :coinId")
    suspend fun deleteByCoinId(coinId: String)
}