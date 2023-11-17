package com.demo.app.countries_list.business

import com.demo.app.shared.business.BaseApiResponseHandler
import com.demo.app.shared.business.ResultHandler
import com.demo.app.countries_list.data.remote.CountriesApiApiRepositoryImpl
import com.demo.app.countries_list.business.model.Country

class CountriesApiResponseHandler(private val apiRepositoryImpl: CountriesApiApiRepositoryImpl) :
    BaseApiResponseHandler() {
    suspend fun getCountryList(): ResultHandler<List<Country>> {
        return makeApiCall { apiRepositoryImpl.getCountryList() }
    }
}