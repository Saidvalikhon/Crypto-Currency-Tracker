package com.example.cryptocurrencytracker.domain.repository

import com.example.cryptocurrencytracker.domain.model.Coin
import com.example.cryptocurrencytracker.domain.model.CoinDetails
import com.example.cryptocurrencytracker.domain.model.CoinWithHistory
import kotlinx.coroutines.flow.Flow

interface CoinRepository {

    fun observeCoins(query: String): Flow<List<Coin>>

    fun observeFavoriteIds(): Flow<Set<String>>

    fun observeCoinWithHistory(coinId: String): Flow<CoinWithHistory?>

    suspend fun refresh()

    suspend fun setFavorite(coinId: String, isFavorite: Boolean)

    suspend fun refreshHistory(coinId: String)

    suspend fun fetchDetails(coinId: String): CoinDetails
}