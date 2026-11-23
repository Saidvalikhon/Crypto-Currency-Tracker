package com.example.cryptocurrencytracker.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cryptocurrencytracker.domain.repository.CoinRepository

class CoinDetailsViewModelFactory(
    private val coinId: String,
    private val repository: CoinRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoinDetailsViewModel::class.java)) {
            return CoinDetailsViewModel(coinId, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}