package com.example.cryptocurrencytracker.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cryptocurrencytracker.domain.repository.CoinRepository

class CoinListViewModelFactory(
    private val repository: CoinRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoinListViewModel::class.java)){
            return CoinListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name} ")
    }
}
