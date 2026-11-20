package com.example.cryptocurrencytracker.data.mapper

import com.example.cryptocurrencytracker.domain.model.AppError
import java.io.IOException
import java.io.InterruptedIOException
import retrofit2.HttpException

fun Throwable.toAppError(): AppError = when (this) {
    is AppError -> this
    is InterruptedIOException -> AppError.Timeout(this)
    is IOException -> AppError.Network(this)
    is HttpException -> AppError.Http(code(), this)
    else -> AppError.Unknown(this)
}