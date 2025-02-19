package com.onespan.android.interview

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class MyApplication : Application() {

    lateinit var connectivityChecker: ConnectivityChecker

    override fun onCreate() {
        super.onCreate()
        connectivityChecker = ConnectivityChecker(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
    }
}

class ConnectivityChecker(private val connectivityManager: ConnectivityManager) {

    fun isInternetAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}