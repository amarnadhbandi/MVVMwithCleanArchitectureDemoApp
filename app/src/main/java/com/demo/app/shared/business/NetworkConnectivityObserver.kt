package com.demo.app.shared.business

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NetworkConnectivityObserver(private val context: Context) {

    private val isNetworkAvailable = MutableStateFlow(false)

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            isNetworkAvailable.value = true
        }

        override fun onLost(network: Network) {
            isNetworkAvailable.value = false
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            isNetworkAvailable.value = false
        }

        override fun onUnavailable() {
            super.onUnavailable()
            isNetworkAvailable.value = false
        }
    }

    fun registerNetworkCallback(): StateFlow<Boolean> {
        val networkRequest = NetworkRequest.Builder().build()
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }
        return isNetworkAvailable.asStateFlow()
    }

    fun unregisterNetworkCallback() {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}