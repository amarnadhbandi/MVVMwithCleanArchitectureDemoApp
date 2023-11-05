package com.demo.app.shared.business

import android.util.Log
import com.demo.app.utils.Constants
import com.demo.app.utils.Constants.API_BACKOFF_EXP
import com.demo.app.utils.Constants.API_CALL_FAILED
import com.demo.app.utils.Constants.API_REQUEST_FAILED_MAX_TRIES
import com.demo.app.utils.Constants.NUMBER_ZERO
import com.demo.app.utils.Constants.ERROR
import kotlinx.coroutines.delay
import java.lang.Exception
import retrofit2.Response
import java.io.IOException

abstract class BaseApiResponseHandler {

    private val TAG = BaseApiResponseHandler::class.java.simpleName

    suspend fun <T> makeApiCall(apiCall: suspend () -> Response<T>): ResultHandler<T> {
        try {
            val response = retryIO(
                maxRetries = Constants.API_MAX_RETRIES,
                initialBackOffTime = Constants.API_INITIAL_RETRY_DELAY,
                maxBackOffTime = Constants.API_MAX_RETRY_DELAY
            ) {
                apiCall()
            }
            Log.d(TAG, "ApiCall() response : $response")
            if(response.isSuccessful){
                val data = response.body()
                data?.let {
                    return ResultHandler.Success(data)
                }
            }
            return onError("${response.code()} ${response.message()}")
        } catch (exception: RetriesExhaustedException) {
            return onError(exception.message ?: exception.localizedMessage)
        } catch (exception: Exception) {
            return onError(exception.message ?: exception.localizedMessage)
        }
    }

    private fun <T> onError(errorMessage: String): ResultHandler<T> =
        ResultHandler.Error("$ERROR $API_CALL_FAILED $errorMessage")

    private suspend fun <T> retryIO(
        maxRetries: Int,
        initialBackOffTime: Long,
        maxBackOffTime: Long,
        block: suspend () -> Response<T>
    ): Response<T> {
        var backOffTime = initialBackOffTime
        var retryCount: Int = NUMBER_ZERO
        while(retryCount < maxRetries && backOffTime < maxBackOffTime ) {
            try {
                Log.d(TAG, "retryIO() : retryCount : ${retryCount++} and backOffTime: $backOffTime")
                val response = block()
                if(response.isSuccessful) {
                    return response
                }
                retryCount++
            } catch (e: IOException) {
                Log.e(TAG, "$ERROR : ${e.localizedMessage}")
            }
            delay(backOffTime)
            backOffTime = (backOffTime * API_BACKOFF_EXP)
        }
        throw RetriesExhaustedException(API_REQUEST_FAILED_MAX_TRIES)
    }

}