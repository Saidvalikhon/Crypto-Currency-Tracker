package com.example.cryptocurrencytracker.ui.common

import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs
import kotlin.math.round

private const val CURRENCY_SYMBOL = "$"

fun formatPrice(price: Double, locale: Locale = Locale.getDefault()): String {
    val fractionDigits = when {
        abs(price) >= 1000 -> 0
        abs(price) >= 1 -> 2
        else -> 4
    }
    val format = NumberFormat.getNumberInstance(locale).apply {
        minimumFractionDigits = fractionDigits
        maximumFractionDigits = fractionDigits
    }
    return CURRENCY_SYMBOL + format.format(price)
}

fun formatPercent(
    value: Double,
    locale: Locale = Locale.getDefault(),
    showSign: Boolean = true
): String {
    val rounded = round(value * 10) / 10
    val format = NumberFormat.getNumberInstance(locale).apply {
        minimumFractionDigits = 1
        maximumFractionDigits = 1
    }
    val number = format.format(abs(rounded))
    return when {
        !showSign -> "$number%"
        rounded > 0 -> "+$number%"
        rounded < 0 -> "-$number%"
        else -> "$number%"
    }
}