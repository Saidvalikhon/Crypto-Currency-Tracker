package com.example.cryptocurrencytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import com.example.cryptocurrencytracker.ui.details.CoinDetailsRoute
import com.example.cryptocurrencytracker.ui.details.CoinDetailsViewModel
import com.example.cryptocurrencytracker.ui.details.CoinDetailsViewModelFactory
import com.example.cryptocurrencytracker.ui.list.CoinListRoute
import com.example.cryptocurrencytracker.ui.list.CoinListViewModel
import com.example.cryptocurrencytracker.ui.list.CoinListViewModelFactory
import com.example.cryptocurrencytracker.ui.theme.CryptocurrencyTrackerTheme

class MainActivity : ComponentActivity() {

    private val container get() = (application as CryptoApp).container

    private val listViewModel: CoinListViewModel by viewModels {
        CoinListViewModelFactory(container.coinRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptocurrencyTrackerTheme {
                var selectedCoinId by rememberSaveable { mutableStateOf<String?>(null) }
                val coinId = selectedCoinId

                if (coinId == null) {
                    CoinListRoute(
                        viewModel = listViewModel,
                        onCoinClick = { selectedCoinId = it }
                    )
                } else {
                    BackHandler { selectedCoinId = null }

                    val detailsViewModel = remember(coinId) {
                        ViewModelProvider(
                            this@MainActivity,
                            CoinDetailsViewModelFactory(coinId, container.coinRepository)
                        )[coinId, CoinDetailsViewModel::class.java]
                    }
                    CoinDetailsRoute(
                        viewModel = detailsViewModel,
                        onBack = { selectedCoinId = null }
                    )
                }
            }
        }
    }
}