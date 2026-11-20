package com.example.cryptocurrencytracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cryptocurrencytracker.ui.theme.CryptocurrencyTrackerTheme

@Composable
fun CoinIcon(
    symbol: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    @Suppress("UNUSED_PARAMETER") imageUrl: String? = null
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
    ) {
        Text(
            text = symbol.take(2).uppercase(),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(name = "BTC")
@Composable
private fun CoinIconPreview() {
    CryptocurrencyTrackerTheme { CoinIcon(symbol = "BTC") }
}

@Preview(name = "Короткий тикер")
@Composable
private fun CoinIconShortPreview() {
    CryptocurrencyTrackerTheme { CoinIcon(symbol = "x") }
}