package com.example.cryptocurrencytracker.ui.common

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.cryptocurrencytracker.R
import com.example.cryptocurrencytracker.domain.model.AppError

fun AppError.toMessage(context: Context): String = when (this) {
    is AppError.Network -> context.getString(R.string.error_network)
    is AppError.Timeout -> context.getString(R.string.error_timeout)
    is AppError.Http ->
        if (code == 429) context.getString(R.string.error_http_rate_limit)
        else context.getString(R.string.error_http, code)
    is AppError.Database -> context.getString(R.string.error_database)
    is AppError.Unknown -> context.getString(R.string.error_unknown)
}

@Composable
fun AppError.toMessage(): String = toMessage(LocalContext.current)