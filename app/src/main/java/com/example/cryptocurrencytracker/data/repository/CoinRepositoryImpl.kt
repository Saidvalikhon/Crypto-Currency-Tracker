package com.example.cryptocurrencytracker.data.repository

import com.example.cryptocurrencytracker.data.local.CoinLocalDataSource
import com.example.cryptocurrencytracker.data.mapper.toAppError
import com.example.cryptocurrencytracker.data.mapper.toCoin
import com.example.cryptocurrencytracker.data.mapper.toDomain
import com.example.cryptocurrencytracker.data.mapper.toEntity
import com.example.cryptocurrencytracker.data.remote.CoinRemoteDataSource
import com.example.cryptocurrencytracker.domain.model.Coin
import com.example.cryptocurrencytracker.domain.repository.CoinRepository
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class CoinRepositoryImpl(
    private val remote: CoinRemoteDataSource,
    private val local: CoinLocalDataSource
) : CoinRepository {

    override fun observeCoins(): Flow<List<Coin>> =
        local.observeCoins()
            .map { entities -> entities.map { it.toCoin() } }
            .catch { e -> throw e.toAppError() }

    override suspend fun refresh() {
        try {
            val coins = remote.getMarkets().toDomain()
            if (coins.isEmpty()) return
            local.replaceAll(coins.map { it.toEntity() })
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw e.toAppError()
        }
    }
}