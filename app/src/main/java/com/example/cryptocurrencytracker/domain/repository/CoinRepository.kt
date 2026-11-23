package com.example.cryptocurrencytracker.domain.repository

import com.example.cryptocurrencytracker.domain.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinRepository {

    fun observeCoins(): Flow<List<Coin>>

    suspend fun refresh()
}