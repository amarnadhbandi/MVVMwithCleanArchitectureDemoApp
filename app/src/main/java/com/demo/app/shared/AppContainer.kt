package com.demo.app.shared

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.demo.app.shared.data.repository.remote.RetrofitFactory
import com.demo.app.shared.business.NetworkConnectionHandler
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

    private val countriesApiService: CountriesApiService = RetrofitFactory.getRetrofitService()
    private val countriesRepositoryImpl = CountriesApiApiRepositoryImpl(countriesApiService)
    private val countriesApiResponseHandler = CountriesApiResponseHandler(countriesRepositoryImpl)

    private val countryDao: CountriesDao = AppDatabase.getDatabase(context).countryDao()
    private val countriesLocalRepository = CountriesDatabaseRepository(countryDao)

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkConnectionHandler = NetworkConnectionHandler(connectivityManager)


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
                networkConnectionHandler = this.networkConnectionHandler
            )
        )[(CountriesListViewModel::class.java)]
    }
}