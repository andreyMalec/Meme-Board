package com.proj.memeboard.service.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> apiCall(apiCall: suspend () -> T): Result<T> =
    withContext(Dispatchers.IO) {
        try {
            Result.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            Result.Failure(throwable)
        }
    }