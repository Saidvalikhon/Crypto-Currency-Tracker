package com.example.cryptocurrencytracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.cryptocurrencytracker.data.fake.FakeCoinRepository
import com.example.cryptocurrencytracker.data.fake.FakeMode
import com.example.cryptocurrencytracker.data.local.AppDatabase
import com.example.cryptocurrencytracker.data.mapper.toCoin
import com.example.cryptocurrencytracker.data.mapper.toEntity
import com.example.cryptocurrencytracker.data.remote.CoinRemoteDataSource
import com.example.cryptocurrencytracker.data.remote.NetworkProvider
import com.example.cryptocurrencytracker.data.repository.CoinRepositoryImpl
import com.example.cryptocurrencytracker.ui.list.CoinListRoute
import com.example.cryptocurrencytracker.ui.list.CoinListViewModel
import com.example.cryptocurrencytracker.ui.list.CoinListViewModelFactory
import com.example.cryptocurrencytracker.ui.theme.CryptocurrencyTrackerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            AppDatabase.NAME
        ).build()
    }

    private val viewModel: CoinListViewModel by viewModels {
        CoinListViewModelFactory(
            CoinRepositoryImpl(CoinRemoteDataSource(NetworkProvider.coinApi))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            lifecycleScope.launch {
                val dao = db.coinDao()

                val coins = FakeCoinRepository(FakeMode.SUCCESS, delayMillis = 0).getCoins()

                dao.upsertAll(coins.map { it.toEntity() })
                Log.d("RoomCheck", "После 1-й записи: ${dao.count()}")

                dao.upsertAll(coins.map { it.toEntity() })
                Log.d("RoomCheck", "После 2-й записи: ${dao.count()}")

                dao.getAll().map { it.toCoin() }.forEach { Log.d("RoomCheck", it.toString()) }

//                 dao.clear()
//                 Log.d("RoomCheck", "После очистки: ${dao.count()}")
            }
        }

        setContent {
            CryptocurrencyTrackerTheme {
                CoinListRoute(
                    viewModel = viewModel,
                    onCoinClick = {  }
                )
            }
        }
    }
}