package com.demo.app.utils

object Constants {
    const val BASE_URL = "https://YourUrl"
    const val ERROR = "Error :"
    const val NO_NETWORK = "no network"
    const val NO_LOCAL_DATA = "no local data available"
    const val API_CALL_FAILED = "Api call failed with"
    const val API_REQUEST_FAILED_MAX_TRIES = "Network request failed after max retries"
    const val APP_CONTAINER_NOT_INIT = "AppContainer is not initialized."
    const val EXCEPTION_EMPTY_LIST = "Empty country list"
    const val EXCEPTION_HANDLED = "Exception handled"
    const val COMMA_OPERATOR = ", "
    const val NUMBER_ZERO = 0
    const val NUMBER_30 = 30
    const val API_BACKOFF_EXP = 2
    const val API_MAX_RETRIES = 3
    const val API_INITIAL_RETRY_DELAY = 1000L
    const val API_MAX_RETRY_DELAY = 16000L

    const val CACHE_CONTROL = "Cache-Control"
    const val CACHE_PUBLIC = "public"
    const val CACHE_MAX_STALE = "max-stale"
    const val CACHE_MAX_STALE_TIME = 60 * 2
    const val CACHE_BUFFER_SIZE_5MB = 5 * 1024 * 1024L

    const val CLIENT_HEADER_STR = "Content-Type"
    const val CLIENT_HEADER_VALUE = "application"
    const val CLIENT_PLATFORM_STR = "X-platform"
    const val CLIENT_PLATFORM_VALUE = "Android"
    const val CLIENT_X_AUTH_STR = "X-Auth-Token"
    const val CLIENT_X_AUTH_DEFAULT = "12121"
}