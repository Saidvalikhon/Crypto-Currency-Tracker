package com.example.cryptocurrencytracker.data.remote.interceptor

import android.util.Log
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Response

class TimingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startNs = System.nanoTime()

        try {
            val response = chain.proceed(request)
            Log.d(TAG, "${request.method} ${request.url} -> ${response.code} за ${elapsedMs(startNs)} мс")
            return response
        } catch (e: IOException) {
            Log.d(TAG, "${request.method} ${request.url} -> сбой за ${elapsedMs(startNs)} мс: ${e.javaClass.simpleName}")
            throw e
        }
    }

    private fun elapsedMs(startNs: Long): Long = (System.nanoTime() - startNs) / 1_000_000

    private companion object {
        const val TAG = "HttpTiming"
    }
}