package com.demo.app.shared.business

import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NetworkConnectionHandler(private val connectivityManager: ConnectivityManager) {
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            isNetworkAvailable.value = true
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            isNetworkAvailable.value = false
        }
    }

    private val isNetworkAvailable = MutableStateFlow(false)

    fun observeNetworkConnectivity(): StateFlow<Boolean> {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        return isNetworkAvailable.asStateFlow()
    }

    fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}