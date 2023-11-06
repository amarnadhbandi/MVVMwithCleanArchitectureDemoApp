package com.demo.app.shared

import android.annotation.SuppressLint
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.demo.app.shared.data.repository.remote.RetrofitFactory
import com.demo.app.shared.business.NetworkConnectivityObserver
import com.demo.app.shared.data.repository.local.AppDatabase
import com.demo.app.countries_list.data.remote.CountriesApiService
import com.demo.app.countries_list.business.CountriesApiResponseHandler
import com.demo.app.countries_list.business.GetCountriesListUseCase
import com.demo.app.countries_list.data.local.CountriesDao
import com.demo.app.countries_list.data.local.CountriesDatabaseRepository
import com.demo.app.countries_list.data.remote.CountriesApiApiRepositoryImpl
import com.demo.app.countries_list.presentation.CountriesListViewModel
import com.demo.app.countries_list.presentation.CountriesListViewModelFactory
import com.demo.app.utils.Constants.APP_CONTAINER_NOT_INIT
import com.demo.app.utils.Constants.ERROR

class AppContainer private constructor(private val context: Context) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: AppContainer? = null
        fun initialize(context: Context) {
            if (instance == null) instance = AppContainer(context)
        }
        fun getInstance(): AppContainer {
            synchronized(this) {
                if (instance == null) {
                    throw IllegalStateException("$ERROR $APP_CONTAINER_NOT_INIT")
                }
                return instance!!
            }
        }
    }

    private val countriesApiService: CountriesApiService = RetrofitFactory.getRetrofitService(context = context)
    private val countriesRepositoryImpl = CountriesApiApiRepositoryImpl(apiService = countriesApiService)
    private val countriesApiResponseHandler = CountriesApiResponseHandler(apiRepositoryImpl = countriesRepositoryImpl)

    private val countriesDao: CountriesDao = AppDatabase.getDatabase(context = context).countryDao()
    private val countriesLocalRepository = CountriesDatabaseRepository(countriesDao = countriesDao)

    private val networkConnectivityObserver = NetworkConnectivityObserver(context = context)

    private fun provideGetGetCountriesListUseCase(): GetCountriesListUseCase {
        return GetCountriesListUseCase(
            apiResponseHandler = this.countriesApiResponseHandler,
            localRepository = this.countriesLocalRepository,
        )
    }
    fun provideCountryListViewModel(fragment: Fragment): CountriesListViewModel {
        return ViewModelProvider(
            fragment,
            CountriesListViewModelFactory(
                getCountriesListUseCase = this.provideGetGetCountriesListUseCase(),
                networkConnectivityObserver = this.networkConnectivityObserver
            )
        )[(CountriesListViewModel::class.java)]
    }
}