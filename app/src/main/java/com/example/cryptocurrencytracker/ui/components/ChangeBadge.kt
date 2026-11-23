package com.example.cryptocurrencytracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cryptocurrencytracker.ui.common.formatPercent
import com.example.cryptocurrencytracker.ui.theme.CryptocurrencyTrackerTheme
import com.example.cryptocurrencytracker.ui.theme.cryptoColors
import kotlin.math.round

@Composable
fun ChangeBadge(
    change: Double,
    modifier: Modifier = Modifier,
    suffix: String = ""
) {
    val crypto = MaterialTheme.cryptoColors
    val colors = MaterialTheme.colorScheme

    val rounded = round(change * 10) / 10

    val arrow: String
    val contentColor = when {
        rounded > 0 -> crypto.positive
        rounded < 0 -> crypto.negative
        else -> colors.onSurfaceVariant
    }
    val containerColor = when {
        rounded > 0 -> crypto.positiveContainer
        rounded < 0 -> crypto.negativeContainer
        else -> colors.surfaceVariant
    }
    arrow = when {
        rounded > 0 -> "▲ "
        rounded < 0 -> "▼ "
        else -> ""
    }

    val suffixText = if (suffix.isEmpty()) "" else " $suffix"

    Text(
        text = arrow + formatPercent(change, showSign = false) + suffixText,
        color = contentColor,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier
            .background(containerColor, RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}

@Preview(name = "Рост")
@Composable
private fun ChangeBadgeUpPreview() {
    CryptocurrencyTrackerTheme { ChangeBadge(change = 1.8) }
}

@Preview(name = "Падение")
@Composable
private fun ChangeBadgeDownPreview() {
    CryptocurrencyTrackerTheme { ChangeBadge(change = -3.1) }
}

@Preview(name = "Ноль")
@Composable
private fun ChangeBadgeZeroPreview() {
    CryptocurrencyTrackerTheme { ChangeBadge(change = 0.0) }
}