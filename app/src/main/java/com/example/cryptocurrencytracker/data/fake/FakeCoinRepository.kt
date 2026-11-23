package com.example.cryptocurrencytracker.data.fake

import com.example.cryptocurrencytracker.domain.model.AppError
import com.example.cryptocurrencytracker.domain.model.Coin
import com.example.cryptocurrencytracker.domain.repository.CoinRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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
    private var callCount = 0

    override fun observeCoins(): Flow<List<Coin>> = coins.asStateFlow()

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

    private val sampleCoins = listOf(
        Coin("bitcoin", "Bitcoin", "BTC", "", 67420.0, 1.8, 1),
        Coin("ethereum", "Ethereum", "ETH", "", 3215.0, -0.6, 2),
        Coin("tether", "Tether", "USDT", "", 1.0, 0.0, 3),
        Coin("binancecoin", "BNB", "BNB", "", 585.0, 2.4, 4),
        Coin("solana", "Solana", "SOL", "", 172.0, -3.1, 5),
        Coin("ripple", "XRP", "XRP", "", 0.52, 0.9, 6)
    )
}