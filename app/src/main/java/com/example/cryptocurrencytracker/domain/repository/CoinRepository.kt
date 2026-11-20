package com.example.cryptocurrencytracker.domain.repository

import com.example.cryptocurrencytracker.domain.model.Coin


interface CoinRepository {
    suspend fun getCoins(): List<Coin>
}