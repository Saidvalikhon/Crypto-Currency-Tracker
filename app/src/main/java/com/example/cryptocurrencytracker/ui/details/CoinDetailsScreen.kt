package com.example.cryptocurrencytracker.ui.details

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cryptocurrencytracker.R
import com.example.cryptocurrencytracker.domain.model.AppError
import com.example.cryptocurrencytracker.domain.model.Coin
import com.example.cryptocurrencytracker.domain.model.CoinDetails
import com.example.cryptocurrencytracker.domain.model.PricePoint
import com.example.cryptocurrencytracker.ui.common.formatCompactPrice
import com.example.cryptocurrencytracker.ui.common.formatPrice
import com.example.cryptocurrencytracker.ui.common.toMessage
import com.example.cryptocurrencytracker.ui.components.ChangeBadge
import com.example.cryptocurrencytracker.ui.components.EmptyContent
import com.example.cryptocurrencytracker.ui.components.ErrorContent
import com.example.cryptocurrencytracker.ui.components.LoadingContent
import com.example.cryptocurrencytracker.ui.components.PriceChart
import com.example.cryptocurrencytracker.ui.theme.CryptocurrencyTrackerTheme

private val ChartHeight = 160.dp

@Composable
fun CoinDetailsScreen(
    state: CoinDetailsUiState,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onFavoriteClick: () -> Unit,
    onPeriodSelected: (ChartPeriod) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val coin = state.coin

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            DetailsTopBar(
                title = coin?.name.orEmpty(),
                isFavorite = coin?.isFavorite == true,
                favoriteEnabled = coin != null,
                onBack = onBack,
                onFavoriteClick = onFavoriteClick
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = onRefresh,
                    enabled = !state.isLoading,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 52.dp)
                ) {
                    Text(
                        text = stringResource(R.string.details_refresh),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when {
                coin != null -> DetailsContent(
                    state = state,
                    coin = coin,
                    onPeriodSelected = onPeriodSelected
                )

                state.error != null -> ErrorContent(
                    message = state.error.toMessage(),
                    onRetry = onRefresh
                )

                state.isLoading || !state.hasLoadedOnce -> LoadingContent()

                else -> EmptyContent(message = stringResource(R.string.details_not_found))
            }
        }
    }
}

@Composable
private fun DetailsTopBar(
    title: String,
    isFavorite: Boolean,
    favoriteEnabled: Boolean,
    onBack: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(R.string.details_back),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onFavoriteClick, enabled = favoriteEnabled) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = stringResource(
                    if (isFavorite) R.string.favorite_remove else R.string.favorite_add
                ),
                tint = if (isFavorite) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                }
            )
        }
    }
}

@Composable
private fun DetailsContent(
    state: CoinDetailsUiState,
    coin: Coin,
    onPeriodSelected: (ChartPeriod) -> Unit
) {
    val points = remember(state.history, state.period) { state.visiblePoints }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(4.dp)) {
            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val label = if (coin.marketCapRank > 0) {
            "${coin.symbol.uppercase()} · #${coin.marketCapRank}"
        } else {
            coin.symbol.uppercase()
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = formatPrice(coin.price),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        ChangeBadge(
            change = coin.priceChangeDay,
            suffix = stringResource(R.string.change_suffix_24h)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ChartCard(
            points = points,
            isLoading = state.isLoading,
            period = state.period,
            onPeriodSelected = onPeriodSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        StatsBlock(details = state.details)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChartCard(
    points: List<PricePoint>,
    isLoading: Boolean,
    period: ChartPeriod,
    onPeriodSelected: (ChartPeriod) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(ChartHeight)
        ) {
            when {
                points.size >= 2 -> PriceChart(
                    points = points,
                    modifier = Modifier.fillMaxSize()
                )

                isLoading -> CircularProgressIndicator()

                else -> Text(
                    text = stringResource(R.string.details_chart_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ChartPeriod.entries.forEach { item ->
                FilterChip(
                    selected = item == period,
                    onClick = { onPeriodSelected(item) },
                    label = { Text(text = stringResource(item.labelRes)) },
                    shape = CircleShape,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }
}

@Composable
private fun StatsBlock(details: CoinDetails?) {
    val none = stringResource(R.string.value_none)

    Column(modifier = Modifier.fillMaxWidth()) {
        StatRow(
            label = stringResource(R.string.details_cap),
            value = details?.marketCap?.let { formatCompactPrice(it) } ?: none
        )
        StatDivider()
        StatRow(
            label = stringResource(R.string.details_volume),
            value = details?.volume24h?.let { formatCompactPrice(it) } ?: none
        )
        StatDivider()
        StatRow(
            label = stringResource(R.string.details_high),
            value = details?.high24h?.let { formatPrice(it) } ?: none
        )
        StatDivider()
        StatRow(
            label = stringResource(R.string.details_low),
            value = details?.low24h?.let { formatPrice(it) } ?: none
        )
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun StatDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outlineVariant)
    )
}

private val previewCoin = Coin("bitcoin", "Bitcoin", "BTC", "", 67420.0, 1.8, 1)

private val previewHistory = List(30) { i ->
    PricePoint(
        timestamp = 1_700_000_000_000L + i * 3_600_000L,
        price = 66_000.0 + i * 50 + if (i % 2 == 0) 150 else -150
    )
}

private val previewDetails = CoinDetails(
    id = "bitcoin",
    description = "",
    marketCap = 1_330_000_000_000.0,
    volume24h = 28_000_000_000.0,
    high24h = 68_100.0,
    low24h = 66_050.0
)

@Preview(name = "Загружено", showBackground = true)
@Composable
private fun CoinDetailsLoadedPreview() {
    CryptocurrencyTrackerTheme {
        CoinDetailsScreen(
            state = CoinDetailsUiState(
                coin = previewCoin,
                history = previewHistory,
                details = previewDetails,
                isLoading = false,
                hasLoadedOnce = true
            ),
            onBack = {},
            onRefresh = {},
            onFavoriteClick = {},
            onPeriodSelected = {}
        )
    }
}

@Preview(name = "Загружено (тёмная)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CoinDetailsLoadedDarkPreview() {
    CryptocurrencyTrackerTheme {
        CoinDetailsScreen(
            state = CoinDetailsUiState(
                coin = previewCoin.copy(isFavorite = true),
                history = previewHistory,
                details = previewDetails,
                isLoading = false,
                hasLoadedOnce = true
            ),
            onBack = {},
            onRefresh = {},
            onFavoriteClick = {},
            onPeriodSelected = {}
        )
    }
}

@Preview(name = "Подробности не загрузились", showBackground = true)
@Composable
private fun CoinDetailsNoDetailsPreview() {
    CryptocurrencyTrackerTheme {
        CoinDetailsScreen(
            state = CoinDetailsUiState(
                coin = previewCoin,
                history = previewHistory,
                isLoading = false,
                hasLoadedOnce = true
            ),
            onBack = {},
            onRefresh = {},
            onFavoriteClick = {},
            onPeriodSelected = {}
        )
    }
}

@Preview(name = "Загрузка", showBackground = true)
@Composable
private fun CoinDetailsLoadingPreview() {
    CryptocurrencyTrackerTheme {
        CoinDetailsScreen(
            state = CoinDetailsUiState(isLoading = true),
            onBack = {},
            onRefresh = {},
            onFavoriteClick = {},
            onPeriodSelected = {}
        )
    }
}

@Preview(name = "Ошибка", showBackground = true)
@Composable
private fun CoinDetailsErrorPreview() {
    CryptocurrencyTrackerTheme {
        CoinDetailsScreen(
            state = CoinDetailsUiState(
                isLoading = false,
                hasLoadedOnce = true,
                error = AppError.Network()
            ),
            onBack = {},
            onRefresh = {},
            onFavoriteClick = {},
            onPeriodSelected = {}
        )
    }
}