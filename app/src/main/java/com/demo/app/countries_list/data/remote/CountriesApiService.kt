package com.demo.app.countries_list.data.remote

import com.demo.app.countries_list.business.model.Country
import retrofit2.Response
import retrofit2.http.GET

interface CountriesApiService {
    @GET("countries.json")
    suspend fun getCountryList(): Response<List<Country>>
}