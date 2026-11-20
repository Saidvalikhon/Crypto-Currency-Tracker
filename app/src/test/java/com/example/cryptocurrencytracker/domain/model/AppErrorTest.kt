package com.example.cryptocurrencytracker.domain.model

import com.example.cryptocurrencytracker.domain.model.AppError
import org.junit.Assert
import org.junit.Test

class AppErrorTest {

    @Test
    fun http_error_keeps_code() {
        val error: AppError = AppError.Http(429)
        Assert.assertTrue(error is AppError.Http)
        Assert.assertEquals(429, (error as AppError.Http).code)
    }

    @Test
    fun error_keeps_cause() {
        val cause = RuntimeException("boom")
        val error = AppError.Network(cause)
        Assert.assertSame(cause, error.cause)
    }
}