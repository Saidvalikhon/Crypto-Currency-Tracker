package com.example.cryptocurrencytracker.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(
    private val apiKey: String? = null
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
            .header("Accept", "application/json")
            .header("User-Agent", "CryptocurrencyTracker/1.0 (Android)")

        if (apiKey != null) {
            builder.header("x-cg-demo-api-key", apiKey)
        }
        return chain.proceed(builder.build())
    }
}