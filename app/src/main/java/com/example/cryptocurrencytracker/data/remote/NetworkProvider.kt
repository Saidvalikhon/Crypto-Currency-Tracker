package com.example.cryptocurrencytracker.data.remote

import com.example.cryptocurrencytracker.BuildConfig
import com.example.cryptocurrencytracker.data.remote.interceptor.HeaderInterceptor
import com.example.cryptocurrencytracker.data.remote.interceptor.TimingInterceptor
import java.util.concurrent.TimeUnit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object NetworkProvider {

    private const val BASE_URL = "https://api.coingecko.com/api/v3/"

    private const val CONNECT_TIMEOUT_SEC = 10L
    private const val READ_TIMEOUT_SEC = 15L
    private const val CALL_TIMEOUT_SEC = 30L

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
            .callTimeout(CALL_TIMEOUT_SEC, TimeUnit.SECONDS)
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(TimingInterceptor())
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BASIC
                        }
                    )
                }
            }
            .build()
    }

    val coinApi: CoinApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(CoinApi::class.java)
    }
}