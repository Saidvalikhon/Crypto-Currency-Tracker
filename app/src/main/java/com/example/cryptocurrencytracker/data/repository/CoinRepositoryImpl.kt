package com.example.cryptocurrencytracker.data.repository

import com.example.cryptocurrencytracker.data.mapper.toAppError
import com.example.cryptocurrencytracker.data.mapper.toDomain
import com.example.cryptocurrencytracker.data.remote.CoinRemoteDataSource
import com.example.cryptocurrencytracker.domain.model.Coin
import com.example.cryptocurrencytracker.domain.repository.CoinRepository
import kotlin.coroutines.cancellation.CancellationException

class CoinRepositoryImpl(
    private val remote: CoinRemoteDataSource
) : CoinRepository {

    override suspend fun getCoins(): List<Coin> {
        try {
            return remote.getMarkets().toDomain()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw e.toAppError()
        }
    }
}