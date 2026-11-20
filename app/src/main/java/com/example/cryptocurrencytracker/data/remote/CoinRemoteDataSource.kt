package com.example.cryptocurrencytracker.data.remote

import com.example.cryptocurrencytracker.data.remote.dto.CoinDto

class CoinRemoteDataSource(
    private val api: CoinApi
) {
    suspend fun getMarkets(): List<CoinDto> = api.getMarkets()
}