package com.proj.memeboard.model.memeApi

import com.proj.memeboard.model.response.LoginResponse
import com.proj.memeboard.model.response.MemeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MemeApi {
    @POST("auth/login")
    fun login(@Query("login") login: String, @Query("password") password: String): Call<LoginResponse>

    @GET("memes")
    fun getMemes(): Call<List<MemeResponse>>
}