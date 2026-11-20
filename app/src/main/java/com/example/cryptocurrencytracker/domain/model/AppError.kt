package com.example.cryptocurrencytracker.domain.model


sealed class AppError(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause) {

    class Network(cause: Throwable? = null) : AppError("Network error", cause)

    class Timeout(cause: Throwable? = null) : AppError("Timeout", cause)

    class Http(val code: Int, cause: Throwable? = null) : AppError("HTTP $code", cause)

    class Database(cause: Throwable? = null) : AppError("Database error", cause)

    class Unknown(cause: Throwable? = null) : AppError("Unknown error", cause)
}