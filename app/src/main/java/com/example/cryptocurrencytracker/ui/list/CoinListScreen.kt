package com.example.cryptocurrencytracker.ui.list

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.cryptocurrencytracker.ui.components.SearchField
import com.example.cryptocurrencytracker.ui.theme.CryptocurrencyTrackerTheme

@Composable
fun CoinListScreen(
    state: CoinListUiState,
    onRetry: () -> Unit,
    onCoinClick: (String) -> Unit,
    onQueryChange: (String) -> Unit,
    onFavoriteClick: (coinId: String, isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.coin_list_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))
                SearchField(
                    query = state.query,
                    onQueryChange = onQueryChange,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when {
                state.error != null && state.coins.isEmpty() -> ErrorContent(
                    message = state.error.toMessage(),
                    onRetry = onRetry
                )

                // спиннер только для первой загрузки, не для поиска
                state.isLoading && state.coins.isEmpty() && state.query.isBlank() ->
                    LoadingContent()

                state.coins.isEmpty() -> EmptyContent(
                    message = stringResource(
                        if (state.query.isBlank()) R.string.coin_list_empty
                        else R.string.coin_list_search_empty
                    )
                )

                else -> LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = state.coins, key = { it.id }) { coin ->
                        CoinCard(
                            coin = coin,
                            onClick = { onCoinClick(coin.id) },
                            // передаём желаемое значение, а не «переключить»
                            onFavoriteClick = { onFavoriteClick(coin.id, !coin.isFavorite) }
                        )
                    }
                }
            }
        }
    }
}

private val previewCoins = listOf(
    Coin("bitcoin", "Bitcoin", "BTC", "", 67420.0, 1.8, 1, isFavorite = true),
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
            onCoinClick = {},
            onQueryChange = {},
            onFavoriteClick = { _, _ -> }
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
            onCoinClick = {},
            onQueryChange = {},
            onFavoriteClick = { _, _ -> }
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
            onCoinClick = {},
            onQueryChange = {},
            onFavoriteClick = { _, _ -> }
        )
    }
}

@Preview(name = "Поиск без результатов", showBackground = true)
@Composable
private fun CoinListSearchEmptyPreview() {
    CryptocurrencyTrackerTheme {
        CoinListScreen(
            state = CoinListUiState(isLoading = false, query = "zzz"),
            onRetry = {},
            onCoinClick = {},
            onQueryChange = {},
            onFavoriteClick = { _, _ -> }
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
            onCoinClick = {},
            onQueryChange = {},
            onFavoriteClick = { _, _ -> }
        )
    }
}