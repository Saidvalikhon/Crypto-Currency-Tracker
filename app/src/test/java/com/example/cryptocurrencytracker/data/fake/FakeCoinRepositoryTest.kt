package com.example.cryptocurrencytracker.data.fake

import com.example.cryptocurrencytracker.domain.model.AppError
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class FakeCoinRepositoryTest {

    @Test
    fun success_mode_returns_coins() = runTest {
        val repo = FakeCoinRepository(FakeMode.SUCCESS)
        val coins = repo.getCoins()
        assertEquals(6, coins.size)
    }

    @Test
    fun success_mode_has_growing_and_falling_coins() = runTest {
        val coins = FakeCoinRepository(FakeMode.SUCCESS).getCoins()
        assertTrue(coins.any { it.priceChange24h > 0 })
        assertTrue(coins.any { it.priceChange24h < 0 })
    }

    @Test
    fun network_mode_throws_network_error() = runTest {
        val repo = FakeCoinRepository(FakeMode.NETWORK_ERROR)
        try {
            repo.getCoins()
            fail("Ожидалась ошибка")
        } catch (e: AppError) {
            assertTrue(e is AppError.Network)
        }
    }

    @Test
    fun rate_limit_mode_throws_http_429() = runTest {
        val repo = FakeCoinRepository(FakeMode.RATE_LIMIT)
        try {
            repo.getCoins()
            fail("Ожидалась ошибка")
        } catch (e: AppError) {
            assertEquals(429, (e as AppError.Http).code)
        }
    }
}