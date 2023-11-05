package com.demo.app.countries_list.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.app.shared.business.NetworkConnectionHandler
import com.demo.app.shared.business.ResultHandler
import com.demo.app.countries_list.business.GetCountriesListUseCase
import com.demo.app.countries_list.data.local.entity.CountryEntity
import com.demo.app.utils.Constants.ERROR
import com.demo.app.utils.Constants.EXCEPTION_HANDLED
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class CountriesListViewModel(
    private var getCountriesListUseCase: GetCountriesListUseCase,
    private val networkConnectionHandler: NetworkConnectionHandler
) : ViewModel() {

    private val TAG = CountriesListViewModel::class.java.simpleName
    private var _countries = MutableLiveData<List<CountryEntity>>()
    private val _uiState = MutableLiveData<CountriesListViewState>(CountriesListViewState.Loading)
    val uiState: MutableLiveData<CountriesListViewState> get() = _uiState
    private var _isApiCallTrigger:Boolean = false
    private var _isNetworkConnected:Boolean = false

    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        error("$EXCEPTION_HANDLED ${throwable.localizedMessage}")
    }

    init {
        viewModelScope.launch {
            networkConnectionHandler.observeNetworkConnectivity().collect { isConnected ->
                _isNetworkConnected = isConnected
                if(isConnected){
                    checkApiCallStatus()
                } else {
                    checkApiCallStatus()
                }
            }
        }
    }

    private fun checkApiCallStatus() {
        when (_uiState.value) {
            is CountriesListViewState.Success ->
                _isApiCallTrigger = true

            is CountriesListViewState.Loading ->
                makeApiCallToGetCountriesList()

            is CountriesListViewState.Error ->
                makeApiCallToGetCountriesList()

            else -> {
                Log.e(TAG, "default case _isNetworkConnected:${_isNetworkConnected} _isApiCallTrigger:${_isApiCallTrigger}")
            }
        }
    }

    @Synchronized
    private fun makeApiCallToGetCountriesList() {
        Log.i(TAG, "makeApiCallToGetCountriesList()")
        if(!_isApiCallTrigger) {
            job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                withContext(Dispatchers.Main) {
                    try {
                        _uiState.value = CountriesListViewState.Loading
                        _isApiCallTrigger = true
                        Log.d(TAG, "callingGetCountryList : UiState.Loading ")
                        getCountriesListUseCase.execute(networkConnectionStatus = _isNetworkConnected)
                            .collect { response ->
                                when (response) {
                                    is ResultHandler.Success -> {
                                        if (response.data!!.isNotEmpty()) {
                                            _countries.value = response.data.toMutableList()
                                            _uiState.value = CountriesListViewState.Success(_countries.value!!)
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
        _isApiCallTrigger = false
        _uiState.value = CountriesListViewState.Error(message)
        Log.e(TAG, "UiState.Error $message")
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        _uiState.value = CountriesListViewState.Loading
        networkConnectionHandler.unregisterNetworkCallback()
    }
}