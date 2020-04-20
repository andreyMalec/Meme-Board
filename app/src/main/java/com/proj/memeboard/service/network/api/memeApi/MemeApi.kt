package com.proj.memeboard.service.network.api.memeApi

import com.proj.memeboard.service.network.response.MemeResponse
import retrofit2.http.GET

interface MemeApi {
    @GET("memes")
    suspend fun getMemes(): List<MemeResponse>
}