package com.demo.app.countries_list.business

import com.demo.app.countries_list.business.model.Country
import retrofit2.Response

interface CountriesApiRepository {
    suspend fun getCountryList(): Response<List<Country>>
}