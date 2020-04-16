package com.proj.memeboard.service.network.repo.memeRepo

import com.proj.memeboard.domain.Meme
import com.proj.memeboard.service.network.Result
import com.proj.memeboard.service.network.api.memeApi.MemeApi
import com.proj.memeboard.service.network.apiCall
import kotlinx.coroutines.Dispatchers

class MemeRepo(private val api: MemeApi) {
    suspend fun getMemes(): Result<List<Meme>> {
        return apiCall(Dispatchers.IO) { api.getMemes().map { it.convert() } }
    }
}