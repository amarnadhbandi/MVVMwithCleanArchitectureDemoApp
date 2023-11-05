package com.demo.app.shared.business

sealed class ResultHandler<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : ResultHandler<T>(data)
    class Error<T>(message: String?, data: T? = null) : ResultHandler<T>(data, message)
}
