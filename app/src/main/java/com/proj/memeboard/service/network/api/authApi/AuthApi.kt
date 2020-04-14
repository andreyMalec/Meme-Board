package com.proj.memeboard.service.network.api.authApi

import com.proj.memeboard.service.network.request.LoginRequest
import com.proj.memeboard.service.network.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/auth/logout")
    fun logout(): Call<Boolean>
}