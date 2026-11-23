package com.example.cryptocurrencytracker.data.remote

import com.example.cryptocurrencytracker.data.remote.dto.CoinDetailsDto
import com.example.cryptocurrencytracker.data.remote.dto.CoinDto
import com.example.cryptocurrencytracker.data.remote.dto.MarketChartDto

class CoinRemoteDataSource(
    private val api: CoinApi
) {
    suspend fun getMarkets(): List<CoinDto> = api.getMarkets()

    suspend fun getCoinDetails(id: String): CoinDetailsDto = api.getCoinDetails(id)

    suspend fun getMarketChart(id: String): MarketChartDto = api.getMarketChart(id)
}