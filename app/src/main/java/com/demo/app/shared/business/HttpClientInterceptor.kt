package com.demo.app.shared.business

import com.demo.app.utils.Constants.CLIENT_HEADER_STR
import com.demo.app.utils.Constants.CLIENT_HEADER_VALUE
import com.demo.app.utils.Constants.CLIENT_PLATFORM_STR
import com.demo.app.utils.Constants.CLIENT_PLATFORM_VALUE
import com.demo.app.utils.Constants.CLIENT_X_AUTH_STR
import com.demo.app.utils.Constants.CLIENT_X_AUTH_DEFAULT
import okhttp3.Interceptor
import okhttp3.Response

class HttpClientInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(CLIENT_HEADER_STR, CLIENT_HEADER_VALUE)
            .addHeader(CLIENT_PLATFORM_STR, CLIENT_PLATFORM_VALUE)
            .addHeader(CLIENT_X_AUTH_STR, CLIENT_X_AUTH_DEFAULT)
            .build()

        return chain.proceed(request)
    }
}