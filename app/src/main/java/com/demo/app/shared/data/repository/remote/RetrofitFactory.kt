package com.demo.app.shared.data.repository.remote

import com.demo.app.countries_list.data.remote.CountriesApiService
import com.demo.app.utils.Constants.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {

    private val okHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(HttpClientInterceptor())
    }.build()

    fun getRetrofitService(): CountriesApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build().create(CountriesApiService::class.java)
    }
}