package com.example.cryptocurrencytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.cryptocurrencytracker.ui.list.CoinListRoute
import com.example.cryptocurrencytracker.ui.list.CoinListViewModel
import com.example.cryptocurrencytracker.ui.list.CoinListViewModelFactory
import com.example.cryptocurrencytracker.ui.theme.CryptocurrencyTrackerTheme

class MainActivity : ComponentActivity() {

    private val viewModel: CoinListViewModel by viewModels {
        val container = (application as CryptoApp).container
        CoinListViewModelFactory(container.coinRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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