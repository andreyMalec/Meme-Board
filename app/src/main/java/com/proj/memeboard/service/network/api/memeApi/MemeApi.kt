package com.proj.memeboard.service.network.api.memeApi

import com.proj.memeboard.service.network.response.MemeResponse
import retrofit2.Call
import retrofit2.http.GET

interface MemeApi {
    @GET("memes")
    fun getMemes(): Call<List<MemeResponse>>
}