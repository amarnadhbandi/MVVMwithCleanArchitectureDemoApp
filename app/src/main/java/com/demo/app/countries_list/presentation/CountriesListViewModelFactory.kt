package com.demo.app.countries_list.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.app.shared.business.NetworkConnectivityObserver
import com.demo.app.countries_list.business.GetCountriesListUseCase

class CountriesListViewModelFactory(
    private val getCountriesListUseCase: GetCountriesListUseCase,
    private val networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountriesListViewModel::class.java)) {
            return CountriesListViewModel(
                getCountriesListUseCase = getCountriesListUseCase,
                networkConnectivityObserver = networkConnectivityObserver
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}