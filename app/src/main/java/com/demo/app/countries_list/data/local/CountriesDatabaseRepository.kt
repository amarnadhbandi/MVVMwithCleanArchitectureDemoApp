package com.demo.app.countries_list.data.local

import com.demo.app.countries_list.business.CountriesLocalRepository
import com.demo.app.countries_list.data.local.entity.CountryEntity

class CountriesDatabaseRepository(private val countriesDao: CountriesDao) :
    CountriesLocalRepository {

    override suspend fun insertCountry(country: CountryEntity) {
        countriesDao.insert(country)
    }

    override suspend fun getAllCountries(): List<CountryEntity> {
        return countriesDao.getAllCountries()
    }
}