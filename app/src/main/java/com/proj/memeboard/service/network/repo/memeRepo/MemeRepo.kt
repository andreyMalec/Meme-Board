package com.proj.memeboard.service.network.repo.memeRepo

import com.proj.memeboard.domain.Meme
import com.proj.memeboard.service.network.RetrofitCallback
import com.proj.memeboard.service.network.api.memeApi.MemeApi
import com.proj.memeboard.service.network.response.MemeResponse

class MemeRepo(private val api: MemeApi) {
    fun getMemes(onDataReceived: (data: Result<List<Meme>>) -> Unit) {
        api.getMemes().enqueue(RetrofitCallback<List<MemeResponse>>(
            { data -> onDataReceived(Result.success(data.map { it.convert() })) },
            { error -> onDataReceived(Result.failure(error)) }
        ))
    }
}