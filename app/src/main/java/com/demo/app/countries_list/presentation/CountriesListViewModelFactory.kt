package com.demo.app.countries_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.app.shared.business.NetworkConnectionHandler
import com.demo.app.countries_list.business.GetCountriesListUseCase

class CountriesListViewModelFactory(
    private val getCountriesListUseCase: GetCountriesListUseCase,
    private val networkConnectionHandler: NetworkConnectionHandler
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountriesListViewModel::class.java)) {
            return CountriesListViewModel(
                getCountriesListUseCase = getCountriesListUseCase,
                networkConnectionHandler = networkConnectionHandler
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}