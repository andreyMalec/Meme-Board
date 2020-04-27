package com.proj.memeboard.service.network.interceptor

import com.proj.memeboard.localStorage.userStorage.UserStorage
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val userStorage: UserStorage) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder().apply {
            header("Authorization", userStorage.getToken())
            method(original.method, original.body)
        }.build()
        return chain.proceed(request)
    }
}