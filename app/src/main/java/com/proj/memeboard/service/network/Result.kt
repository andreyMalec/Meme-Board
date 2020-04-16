package com.proj.memeboard.service.network

sealed class Result<out T> {
    data class Success<out T>(val value: T) : Result<T>()
    data class Failure(val code: Int? = null, val error: String? = null) : Result<Nothing>()
    object NetworkError : Result<Nothing>()
}