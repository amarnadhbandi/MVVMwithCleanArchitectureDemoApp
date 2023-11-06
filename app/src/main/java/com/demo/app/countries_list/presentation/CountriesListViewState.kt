package com.demo.app.countries_list.presentation

import com.demo.app.countries_list.data.local.entity.CountryEntity

sealed class CountriesListViewState {
    object Loading : CountriesListViewState()
    data class Success(val data: List<CountryEntity>) : CountriesListViewState()
    data class LocalData(val data: List<CountryEntity>) : CountriesListViewState()

    data class Error(val message: String) : CountriesListViewState()
}
