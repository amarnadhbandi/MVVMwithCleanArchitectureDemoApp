package com.demo.app.countries_list.business

import android.util.Log
import com.demo.app.shared.business.ResultHandler
import com.demo.app.countries_list.business.DomainToEntityMapper.toEntityList
import com.demo.app.countries_list.data.local.entity.CountryEntity
import com.demo.app.utils.Constants.ERROR
import com.demo.app.utils.Constants.EXCEPTION_EMPTY_LIST
import com.demo.app.utils.Constants.NO_LOCAL_DATA
import com.demo.app.utils.Constants.NO_NETWORK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class GetCountriesListUseCase(
    private val apiResponseHandler: CountriesApiResponseHandler,
    private val localRepository: CountriesLocalRepository
) {
    private val TAG = GetCountriesListUseCase::class.java.simpleName

    suspend fun execute(networkConnectionStatus: Boolean): Flow<ResultHandler<List<CountryEntity>>> {
        return withContext(Dispatchers.IO) {
            flow {
                try {
                    Log.i(TAG,"execute() : networkConnectionStatus $networkConnectionStatus")
                    if (networkConnectionStatus) {
                        fetchFromRemoteApiAndCache().collect() { response ->
                            Log.d(TAG, "execute data:  ${response.data}")
                            Log.d(TAG, "execute message:  ${response.message}")
                            when (response) {
                                is ResultHandler.Success -> {
                                    response.data?.let {
                                        emit(ResultHandler.Success(it))
                                    }
                                }
                                is ResultHandler.Error -> {
                                    response.message?.let {
                                        emit(fetchFromLocalDataBase(it))
                                    }
                                }
                            }
                        }
                    } else {
                        emit(fetchFromLocalDataBase("$ERROR $NO_NETWORK"))
                    }
                } catch (e: Exception) {
                    Log.e(this.javaClass.simpleName, "$ERROR ${e.localizedMessage}")
                    emit(fetchFromLocalDataBase("$ERROR ${e.localizedMessage}"))
                }
            }.flowOn(Dispatchers.IO)
        }
    }

    private suspend fun fetchFromLocalDataBase(message: String): ResultHandler<List<CountryEntity>> {
        val localResponse = localRepository.getAllCountries()
        if (localResponse.isNotEmpty()) {
            return ResultHandler.Success(localResponse)
        }
        return ResultHandler.Error(message.ifEmpty { "$ERROR $NO_LOCAL_DATA" })
    }

    private suspend fun fetchFromRemoteApiAndCache(): Flow<ResultHandler<List<CountryEntity>>> {
        return withContext(Dispatchers.IO) {
            flow {
                try {
                    Log.i(TAG, "fetchFromRemoteApiAndCache()")
                    apiResponseHandler.getCountryList().collect() { response ->
                        when (response) {
                            is ResultHandler.Success -> {
                                if (response.data!!.isNotEmpty()) {
                                    val resultHandler = ResultHandler.Success(response.data.toEntityList())
                                    emit(resultHandler)
                                    resultHandler.data?.map {
                                        localRepository.insertCountry(it)
                                    }
                                } else if (response.data.isNullOrEmpty() || response.message!!.isNotEmpty()) {
                                    emit(ResultHandler.Error(response.message?: IllegalStateException(EXCEPTION_EMPTY_LIST).localizedMessage))
                                }
                            }
                            is ResultHandler.Error -> {
                                if (response.message!!.isNotEmpty()) {
                                    emit(ResultHandler.Error(response.message))
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e(this.javaClass.simpleName, "$ERROR ${e.localizedMessage}")
                    emit(ResultHandler.Error("$ERROR ${e.localizedMessage}"))
                }
            }.flowOn(Dispatchers.IO)
        }
    }
}
