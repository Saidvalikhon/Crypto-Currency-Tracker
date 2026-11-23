package com.example.cryptocurrencytracker.data.fake

import com.example.cryptocurrencytracker.domain.model.AppError
import com.example.cryptocurrencytracker.domain.model.Coin
import com.example.cryptocurrencytracker.domain.model.CoinDetails
import com.example.cryptocurrencytracker.domain.model.CoinWithHistory
import com.example.cryptocurrencytracker.domain.model.PricePoint
import com.example.cryptocurrencytracker.domain.repository.CoinRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

enum class FakeMode {
    SUCCESS,
    NETWORK_ERROR,
    TIMEOUT,
    RATE_LIMIT,
    ALTERNATING
}

class FakeCoinRepository(
    private val mode: FakeMode = FakeMode.SUCCESS,
    private val delayMillis: Long = 1500L
) : CoinRepository {

    private val coins = MutableStateFlow<List<Coin>>(emptyList())
    private val favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    private var callCount = 0

    override fun observeCoins(query: String): Flow<List<Coin>> =
        coins.map { list ->
            if (query.isBlank()) {
                list
            } else {
                list.filter {
                    it.name.contains(query, ignoreCase = true) ||
                            it.symbol.contains(query, ignoreCase = true)
                }
            }
        }

    override fun observeFavoriteIds(): Flow<Set<String>> = favoriteIds

    override fun observeCoinWithHistory(coinId: String): Flow<CoinWithHistory?> =
        coins.map { list ->
            list.firstOrNull { it.id == coinId }
                ?.let { CoinWithHistory(coin = it, history = sampleHistory) }
        }

    override suspend fun refresh() {
        delay(delayMillis)
        callCount++

        when (mode) {
            FakeMode.SUCCESS -> Unit
            FakeMode.NETWORK_ERROR -> throw AppError.Network()
            FakeMode.TIMEOUT -> throw AppError.Timeout()
            FakeMode.RATE_LIMIT -> throw AppError.Http(429)
            FakeMode.ALTERNATING -> if (callCount % 2 == 0) throw AppError.Network()
        }

        coins.value = sampleCoins
    }

    override suspend fun setFavorite(coinId: String, isFavorite: Boolean) {
        favoriteIds.update { if (isFavorite) it + coinId else it - coinId }
    }

    override suspend fun refreshHistory(coinId: String) {
        delay(delayMillis)
    }

    override suspend fun fetchDetails(coinId: String): CoinDetails {
        delay(delayMillis)
        return CoinDetails(
            id = coinId,
            description = "Тестовое описание монеты $coinId",
            marketCap = 1_330_000_000_000.0,
            volume24h = 28_000_000_000.0,
            high24h = 68_100.0,
            low24h = 66_050.0
        )
    }

    private val sampleCoins = listOf(
        Coin("bitcoin", "Bitcoin", "BTC", "", 67420.0, 1.8, 1),
        Coin("ethereum", "Ethereum", "ETH", "", 3215.0, -0.6, 2),
        Coin("tether", "Tether", "USDT", "", 1.0, 0.0, 3),
        Coin("binancecoin", "BNB", "BNB", "", 585.0, 2.4, 4),
        Coin("solana", "Solana", "SOL", "", 172.0, -3.1, 5),
        Coin("ripple", "XRP", "XRP", "", 0.52, 0.9, 6)
    )

    private val sampleHistory = List(24) { i ->
        PricePoint(
            timestamp = 1_700_000_000_000L + i * 3_600_000L,
            price = 67_000.0 + i * 25
        )
    }
}