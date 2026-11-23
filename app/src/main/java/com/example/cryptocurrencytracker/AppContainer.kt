package com.example.cryptocurrencytracker

import android.content.Context
import androidx.room.Room
import com.example.cryptocurrencytracker.data.local.AppDatabase
import com.example.cryptocurrencytracker.data.local.CoinLocalDataSource
import com.example.cryptocurrencytracker.data.remote.CoinRemoteDataSource
import com.example.cryptocurrencytracker.data.remote.NetworkProvider
import com.example.cryptocurrencytracker.data.repository.CoinRepositoryImpl
import com.example.cryptocurrencytracker.domain.repository.CoinRepository

class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(appContext, AppDatabase::class.java, AppDatabase.NAME).build()
    }

    private val remote by lazy { CoinRemoteDataSource(NetworkProvider.coinApi) }
    private val local by lazy {
        CoinLocalDataSource(
            db = database,
            dao = database.coinDao(),
            favoriteDao = database.favoriteDao(),
            priceHistoryDao = database.priceHistoryDao()
        )
    }

    val coinRepository: CoinRepository by lazy { CoinRepositoryImpl(remote, local) }
}