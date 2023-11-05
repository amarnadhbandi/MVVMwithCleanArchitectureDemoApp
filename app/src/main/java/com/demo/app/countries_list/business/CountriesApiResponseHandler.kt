package com.demo.app.countries_list.business

import com.demo.app.shared.business.BaseApiResponseHandler
import com.demo.app.shared.business.ResultHandler
import com.demo.app.countries_list.data.remote.CountriesApiApiRepositoryImpl
import com.demo.app.countries_list.business.model.Country
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext


class CountriesApiResponseHandler(private val apiRepositoryImpl: CountriesApiApiRepositoryImpl) :
    BaseApiResponseHandler() {
    suspend fun getCountryList(): Flow<ResultHandler<List<Country>>> {
        return withContext(Dispatchers.IO) {
            flow {
                emit(makeApiCall { apiRepositoryImpl.getCountryList() })
            }.flowOn(Dispatchers.IO)
        }
    }

}