package com.example.cryptocurrencytracker.data.fake

import com.example.cryptocurrencytracker.domain.model.AppError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class FakeCoinRepositoryTest {

    @Test
    fun before_refresh_there_are_no_coins() = runTest {
        val repo = FakeCoinRepository(FakeMode.SUCCESS)
        assertTrue(repo.observeCoins().first().isEmpty())
    }

    @Test
    fun success_refresh_fills_the_flow() = runTest {
        val repo = FakeCoinRepository(FakeMode.SUCCESS)
        repo.refresh()
        assertEquals(6, repo.observeCoins().first().size)
    }

    @Test
    fun coins_include_growing_and_falling() = runTest {
        val repo = FakeCoinRepository(FakeMode.SUCCESS)
        repo.refresh()
        val coins = repo.observeCoins().first()
        assertTrue(coins.any { it.priceChangeDay > 0 })
        assertTrue(coins.any { it.priceChangeDay < 0 })
    }

    @Test
    fun network_mode_throws_and_keeps_flow_empty() = runTest {
        val repo = FakeCoinRepository(FakeMode.NETWORK_ERROR)
        try {
            repo.refresh()
            fail("Ожидалась ошибка")
        } catch (e: AppError) {
            assertTrue(e is AppError.Network)
        }
        assertTrue(repo.observeCoins().first().isEmpty())
    }

    @Test
    fun rate_limit_mode_throws_http_429() = runTest {
        val repo = FakeCoinRepository(FakeMode.RATE_LIMIT)
        try {
            repo.refresh()
            fail("Ожидалась ошибка")
        } catch (e: AppError) {
            assertEquals(429, (e as AppError.Http).code)
        }
    }
}