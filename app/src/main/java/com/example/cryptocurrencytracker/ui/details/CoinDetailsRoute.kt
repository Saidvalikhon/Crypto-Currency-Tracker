package com.example.cryptocurrencytracker.ui.details

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.cryptocurrencytracker.R
import com.example.cryptocurrencytracker.ui.common.toMessage

@Composable
fun CoinDetailsRoute(
    viewModel: CoinDetailsViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    is CoinDetailsEvent.ShowError -> {
                        val result = snackbarHostState.showSnackbar(
                            message = event.error.toMessage(context),
                            actionLabel = context.getString(R.string.action_retry),
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.load()
                        }
                    }
                }
            }
        }
    }

    CoinDetailsScreen(
        state = state,
        onBack = onBack,
        onRefresh = viewModel::load,
        onFavoriteClick = viewModel::onFavoriteClick,
        onPeriodSelected = viewModel::onPeriodSelected,
        snackbarHostState = snackbarHostState
    )
}