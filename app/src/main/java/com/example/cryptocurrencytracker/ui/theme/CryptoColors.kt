package com.example.cryptocurrencytracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CryptoColors(
    val positive: Color,
    val positiveContainer: Color,
    val negative: Color,
    val negativeContainer: Color
)

val LightCryptoColors = CryptoColors(
    positive = LightPositive,
    positiveContainer = LightPositiveContainer,
    negative = LightNegative,
    negativeContainer = LightNegativeContainer
)

val DarkCryptoColors = CryptoColors(
    positive = DarkPositive,
    positiveContainer = DarkPositiveContainer,
    negative = DarkNegative,
    negativeContainer = DarkNegativeContainer
)

val LocalCryptoColors = staticCompositionLocalOf { LightCryptoColors }

// Использование: MaterialTheme.cryptoColors.positive
val MaterialTheme.cryptoColors: CryptoColors
    @Composable
    @ReadOnlyComposable
    get() = LocalCryptoColors.current