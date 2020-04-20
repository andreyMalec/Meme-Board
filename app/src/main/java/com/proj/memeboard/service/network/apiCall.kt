package com.proj.memeboard.service.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> apiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): Result<T> {
    return withContext(dispatcher) {
        try {
            Result.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> Result.NetworkError
                is HttpException -> Result.Failure(throwable.code(), throwable.message())
                is ArrayIndexOutOfBoundsException -> Result.Failure(204, "no content")//empty body response

                else -> Result.Failure(null, null)
            }
        }
    }
}