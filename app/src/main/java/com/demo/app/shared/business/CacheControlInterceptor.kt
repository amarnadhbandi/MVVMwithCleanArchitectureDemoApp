package com.demo.app.shared.business

import com.demo.app.utils.Constants.CACHE_CONTROL
import com.demo.app.utils.Constants.CACHE_MAX_STALE
import com.demo.app.utils.Constants.CACHE_MAX_STALE_TIME
import com.demo.app.utils.Constants.CACHE_PUBLIC
import okhttp3.Interceptor
import okhttp3.Response

class CacheControlInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()
            .header(CACHE_CONTROL, "$CACHE_PUBLIC, $CACHE_MAX_STALE= $CACHE_MAX_STALE_TIME")
            .build()

        val response = chain.proceed(request)
        return response.newBuilder()
            .removeHeader("Pragma")
            .build()
    }
}