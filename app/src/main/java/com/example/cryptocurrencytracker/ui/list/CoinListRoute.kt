package com.example.cryptocurrencytracker.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CoinListRoute(
    viewModel: CoinListViewModel,
    onCoinClick: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CoinListScreen(
        state = state,
        onRetry = viewModel::refresh,
        onCoinClick = onCoinClick
    )
}