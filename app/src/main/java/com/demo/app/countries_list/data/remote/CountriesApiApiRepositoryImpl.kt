package com.demo.app.countries_list.data.remote

import com.demo.app.countries_list.business.CountriesApiRepository
import com.demo.app.countries_list.business.model.Country
import retrofit2.Response

class CountriesApiApiRepositoryImpl(private val apiService: CountriesApiService) :
    CountriesApiRepository {
    override suspend fun getCountryList(): Response<List<Country>> {
        return apiService.getCountryList()
    }
}