package com.example.cryptocurrencytracker.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.cryptocurrencytracker.R
import com.example.cryptocurrencytracker.domain.model.AppError

@Composable
fun AppError.toMessage(): String = when (this){
    is AppError.Network -> stringResource(R.string.error_network)
    is AppError.Timeout -> stringResource(R.string.error_timeout)
    is AppError.Http ->
        if (code == 429) stringResource(R.string.error_http_rate_limit)
        else stringResource(R.string.error_http, code)
    is AppError.Database -> stringResource(R.string.error_database)
    is AppError.Unknown -> stringResource(R.string.error_unknown)
}
