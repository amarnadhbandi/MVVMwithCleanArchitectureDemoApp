package com.demo.app.countries_list.business

import com.demo.app.countries_list.data.local.entity.CountryEntity

interface CountriesLocalRepository {
    suspend fun insertCountry(country: CountryEntity)
    suspend fun getAllCountries(): List<CountryEntity>
}
