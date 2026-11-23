package com.example.cryptocurrencytracker.ui.common

import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

private const val CURRENCY_SYMBOL = "$"
private const val TRILLION = 1_000_000_000_000.0
private const val BILLION = 1_000_000_000.0
private const val MILLION = 1_000_000.0

fun formatCompactPrice(value: Double, locale: Locale = Locale.getDefault()): String {
    val isRussian = locale.language == "ru"
    val (divider, suffix) = when {
        abs(value) >= TRILLION -> TRILLION to (if (isRussian) "трлн" else "T")
        abs(value) >= BILLION -> BILLION to (if (isRussian) "млрд" else "B")
        abs(value) >= MILLION -> MILLION to (if (isRussian) "млн" else "M")
        else -> return formatPrice(value, locale)
    }
    val format = NumberFormat.getNumberInstance(locale).apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 2
    }
    return CURRENCY_SYMBOL + format.format(value / divider) + " " + suffix
}