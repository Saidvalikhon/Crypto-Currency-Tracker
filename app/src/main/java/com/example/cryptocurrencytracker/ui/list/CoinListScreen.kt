package com.example.cryptocurrencytracker.ui.list

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cryptocurrencytracker.R
import com.example.cryptocurrencytracker.domain.model.AppError
import com.example.cryptocurrencytracker.domain.model.Coin
import com.example.cryptocurrencytracker.ui.common.toMessage
import com.example.cryptocurrencytracker.ui.components.CoinCard
import com.example.cryptocurrencytracker.ui.components.EmptyContent
import com.example.cryptocurrencytracker.ui.components.ErrorContent
import com.example.cryptocurrencytracker.ui.components.LoadingContent
import com.example.cryptocurrencytracker.ui.theme.CryptocurrencyTrackerTheme

@Composable
fun CoinListScreen(
    state: CoinListUiState,
    onRetry: () -> Unit,
    onCoinClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Text(
                text = stringResource(R.string.coin_list_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when {
                state.error != null && state.coins.isEmpty() -> ErrorContent(
                    message = state.error.toMessage(),
                    onRetry = onRetry
                )

                state.isLoading && state.coins.isEmpty() -> LoadingContent()

                state.coins.isEmpty() -> EmptyContent(
                    message = stringResource(R.string.coin_list_empty)
                )

                else -> LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = state.coins, key = { it.id }) { coin ->
                        CoinCard(
                            coin = coin,
                            onClick = { onCoinClick(coin.id) }
                        )
                    }
                }
            }
        }
    }
}


private val previewCoins = listOf(
    Coin("bitcoin", "Bitcoin", "BTC", "", 67420.0, 1.8, 1),
    Coin("ethereum", "Ethereum", "ETH", "", 3215.0, -0.6, 2),
    Coin("tether", "Tether", "USDT", "", 1.0, 0.0, 3),
    Coin("solana", "Solana", "SOL", "", 172.0, -3.1, 5),
    Coin("ripple", "XRP", "XRP", "", 0.52, 0.9, 6)
)

@Preview(name = "Loading", showBackground = true)
@Composable
private fun CoinListLoadingPreview() {
    CryptocurrencyTrackerTheme {
        CoinListScreen(
            state = CoinListUiState(isLoading = true),
            onRetry = {},
            onCoinClick = {}
        )
    }
}

@Preview(name = "Success", showBackground = true)
@Composable
private fun CoinListSuccessPreview() {
    CryptocurrencyTrackerTheme {
        CoinListScreen(
            state = CoinListUiState(isLoading = false, coins = previewCoins),
            onRetry = {},
            onCoinClick = {}
        )
    }
}

@Preview(name = "Success (тёмная)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CoinListSuccessDarkPreview() {
    CryptocurrencyTrackerTheme {
        CoinListScreen(
            state = CoinListUiState(isLoading = false, coins = previewCoins),
            onRetry = {},
            onCoinClick = {}
        )
    }
}

@Preview(name = "Error", showBackground = true)
@Composable
private fun CoinListErrorPreview() {
    CryptocurrencyTrackerTheme {
        CoinListScreen(
            state = CoinListUiState(isLoading = false, error = AppError.Network()),
            onRetry = {},
            onCoinClick = {}
        )
    }
}