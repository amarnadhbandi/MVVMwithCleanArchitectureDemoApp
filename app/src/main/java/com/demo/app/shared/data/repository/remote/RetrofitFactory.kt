package com.demo.app.shared.data.repository.remote

import android.content.Context
import com.demo.app.countries_list.data.remote.CountriesApiService
import com.demo.app.shared.business.CacheControlInterceptor
import com.demo.app.shared.business.HttpClientInterceptor
import com.demo.app.utils.Constants.BASE_URL
import com.demo.app.utils.Constants.CACHE_BUFFER_SIZE_5MB
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {
    fun getRetrofitService(context: Context): CountriesApiService {

        val cache = Cache(context.cacheDir, CACHE_BUFFER_SIZE_5MB)
        val okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(HttpClientInterceptor())
            addNetworkInterceptor(CacheControlInterceptor())
            cache(cache)
        }.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build().create(CountriesApiService::class.java)
    }
}