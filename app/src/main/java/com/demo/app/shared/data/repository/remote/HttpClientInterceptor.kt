package com.demo.app.shared.data.repository.remote

import okhttp3.Interceptor
import okhttp3.Response

class HttpClientInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Content-Type","application/json")
            .addHeader("X-platform","Android")
            .addHeader("X-Auth-Token","1111111")
            .build()

        return chain.proceed(request)
    }
}