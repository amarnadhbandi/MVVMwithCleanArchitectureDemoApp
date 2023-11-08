package com.demo.app.countries_list.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.app.shared.business.NetworkConnectivityObserver
import com.demo.app.shared.business.ResultHandler
import com.demo.app.countries_list.business.GetCountriesListUseCase
import com.demo.app.countries_list.data.local.entity.CountryEntity
import com.demo.app.countries_list.presentation.CountriesListViewState.Success
import com.demo.app.utils.Constants.ERROR
import com.demo.app.utils.Constants.EXCEPTION_HANDLED
import com.demo.app.utils.Constants.NO_NETWORK
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class CountriesListViewModel(
    private var getCountriesListUseCase: GetCountriesListUseCase,
    private val networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val TAG = CountriesListViewModel::class.java.simpleName
    private var _countries = MutableLiveData<List<CountryEntity>>()
    private val _uiState = MutableLiveData<CountriesListViewState>(CountriesListViewState.Loading)
    val uiState: MutableLiveData<CountriesListViewState> get() = _uiState

    private var isApiFetchDataTrigger: Boolean = false
    private var isNetworkConnected: Boolean = false

    private var networkObserverJob: Job? = null
    private var apiRequestJob: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        error("$EXCEPTION_HANDLED ${throwable.localizedMessage}")
    }

    init {
        networkObserverJob = viewModelScope.launch {
            networkConnectivityObserver.registerNetworkCallback().collect { isConnected ->
                isNetworkConnected = isConnected
                Log.d(TAG, "isNetworkConnected $isNetworkConnected")
                checkApiRequestStatus()
            }
        }
    }

    private fun checkApiRequestStatus() {
        if (isNetworkConnected) {
            makeApiCallToGetCountriesList()
        } else {
            when (uiState.value) {
                is Success -> {
                    Log.i(TAG, "Data Already Available")
                }
                else -> {
                    if (!isApiFetchDataTrigger)
                        uiState.value = CountriesListViewState.Error(NO_NETWORK)
                }
            }
        }
    }

    @Synchronized
    private fun makeApiCallToGetCountriesList() {
        if (!isApiFetchDataTrigger) {
            apiRequestJob = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                withContext(Dispatchers.Main) {
                    try {
                        _uiState.value = CountriesListViewState.Loading
                        isApiFetchDataTrigger = true
                        getCountriesListUseCase.execute(networkConnectionStatus = isNetworkConnected)
                            .collect { response ->
                                when (response) {
                                    is ResultHandler.Success -> {
                                        if (response.data!!.isNotEmpty()) {
                                            _countries.value = response.data.toMutableList()
                                            _uiState.value = Success(_countries.value!!)
                                            isApiFetchDataTrigger = isNetworkConnected
                                        }
                                    }

                                    is ResultHandler.Error -> {
                                        error(response.message!!)
                                    }
                                }
                            }

                    } catch (e: Exception) {
                        error("$ERROR : ${e.localizedMessage}")
                    }
                }
            }
        }
    }

    private fun error(message: String) {
        isApiFetchDataTrigger = false
        _uiState.value = CountriesListViewState.Error(message)
        Log.e(TAG, "UiState.Error $message")
    }

    override fun onCleared() {
        super.onCleared()
        networkConnectivityObserver.unregisterNetworkCallback()
        networkObserverJob?.cancel()
        apiRequestJob?.cancel()
    }
}