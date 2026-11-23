package com.example.cryptocurrencytracker

import android.app.Application

class CryptoApp : Application() {

    val container: AppContainer by lazy { AppContainer(this) }
}