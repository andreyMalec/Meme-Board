package com.proj.memeboard.model.memeApi

import com.proj.memeboard.model.response.LoginResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface MemeApi {
    @POST("auth/login")
    fun login(@Query("login") login: String, @Query("password") password: String): Call<LoginResponse>
}