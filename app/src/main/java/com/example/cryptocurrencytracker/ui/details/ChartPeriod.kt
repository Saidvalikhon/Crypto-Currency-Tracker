package com.example.cryptocurrencytracker.ui.details

import androidx.annotation.StringRes
import com.example.cryptocurrencytracker.R

enum class ChartPeriod(val hours: Int, @StringRes val labelRes: Int) {
    DAY(24, R.string.period_24h),
    WEEK(24 * 7, R.string.period_7d),
    MONTH(24 * 30, R.string.period_30d)
}