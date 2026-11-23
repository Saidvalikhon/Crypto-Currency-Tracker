package com.example.cryptocurrencytracker.data.repository

import com.example.cryptocurrencytracker.data.local.CoinLocalDataSource
import com.example.cryptocurrencytracker.data.mapper.toAppError
import com.example.cryptocurrencytracker.data.mapper.toCoin
import com.example.cryptocurrencytracker.data.mapper.toCoinDetails
import com.example.cryptocurrencytracker.data.mapper.toCoinWithHistory
import com.example.cryptocurrencytracker.data.mapper.toDomain
import com.example.cryptocurrencytracker.data.mapper.toEntity
import com.example.cryptocurrencytracker.data.mapper.toHistoryEntities
import com.example.cryptocurrencytracker.data.remote.CoinRemoteDataSource
import com.example.cryptocurrencytracker.domain.model.Coin
import com.example.cryptocurrencytracker.domain.model.CoinDetails
import com.example.cryptocurrencytracker.domain.model.CoinWithHistory
import com.example.cryptocurrencytracker.domain.repository.CoinRepository
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class CoinRepositoryImpl(
    private val remote: CoinRemoteDataSource,
    private val local: CoinLocalDataSource
) : CoinRepository {

    override fun observeCoins(query: String): Flow<List<Coin>> =
        local.observeCoins(query)
            .map { entities -> entities.map { it.toCoin() } }
            .catch { e -> throw e.toAppError() }

    override fun observeFavoriteIds(): Flow<Set<String>> =
        local.observeFavoriteIds()
            .catch { e -> throw e.toAppError() }

    override fun observeCoinWithHistory(coinId: String): Flow<CoinWithHistory?> =
        local.observeCoinWithHistory(coinId)
            .map { entity -> entity?.toCoinWithHistory() }
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

    override suspend fun setFavorite(coinId: String, isFavorite: Boolean) {
        try {
            local.setFavorite(coinId, isFavorite)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw e.toAppError()
        }
    }

    override suspend fun refreshHistory(coinId: String) {
        try {
            val points = remote.getMarketChart(coinId).toHistoryEntities(coinId)
            if (points.isEmpty()) return
            local.replaceHistory(coinId, points)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw e.toAppError()
        }
    }

    override suspend fun fetchDetails(coinId: String): CoinDetails {
        try {
            return remote.getCoinDetails(coinId).toCoinDetails()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw e.toAppError()
        }
    }
}