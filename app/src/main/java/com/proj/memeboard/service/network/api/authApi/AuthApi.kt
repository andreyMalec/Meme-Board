package com.proj.memeboard.service.network.api.authApi

import com.proj.memeboard.service.network.request.LoginRequest
import com.proj.memeboard.service.network.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("/auth/logout")
    suspend fun logout(): Nothing
}