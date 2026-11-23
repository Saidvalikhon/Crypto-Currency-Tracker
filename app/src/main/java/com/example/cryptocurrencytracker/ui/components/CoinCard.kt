package com.example.cryptocurrencytracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cryptocurrencytracker.R
import com.example.cryptocurrencytracker.domain.model.Coin
import com.example.cryptocurrencytracker.ui.common.formatPrice
import com.example.cryptocurrencytracker.ui.theme.CryptocurrencyTrackerTheme

@Composable
fun CoinCard(
    coin: Coin,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .heightIn(min = 64.dp)
            .padding(start = 14.dp, top = 12.dp, bottom = 12.dp, end = 4.dp)
    ) {
        Text(
            text = coin.marketCapRank.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.widthIn(min = 20.dp)
        )

        CoinIcon(symbol = coin.symbol, imageUrl = coin.imageUrl)

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = coin.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = coin.symbol.uppercase(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formatPrice(coin.price),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            ChangeBadge(change = coin.priceChangeDay)
        }

        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = stringResource(
                    if (coin.isFavorite) R.string.favorite_remove else R.string.favorite_add
                ),
                tint = if (coin.isFavorite) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                }
            )
        }
    }
}

@Preview(name = "Рост")
@Composable
private fun CoinCardUpPreview() {
    CryptocurrencyTrackerTheme {
        CoinCard(
            coin = Coin("bitcoin", "Bitcoin", "btc", "", 67420.0, 1.8, 1),
            onClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(name = "Избранное")
@Composable
private fun CoinCardFavoritePreview() {
    CryptocurrencyTrackerTheme {
        CoinCard(
            coin = Coin("bitcoin", "Bitcoin", "btc", "", 67420.0, 1.8, 1, isFavorite = true),
            onClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(name = "Падение, длинное имя")
@Composable
private fun CoinCardDownPreview() {
    CryptocurrencyTrackerTheme {
        CoinCard(
            coin = Coin("long", "Very Long Coin Name Protocol Token", "vlcnp", "", 0.52, -3.1, 120),
            onClick = {},
            onFavoriteClick = {}
        )
    }
}