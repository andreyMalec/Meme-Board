package com.proj.memeboard.model.memeRepo

import com.proj.memeboard.model.response.MemeResponse
import com.proj.memeboard.domain.User
import com.proj.memeboard.model.request.LoginRequest
import com.proj.memeboard.model.RetrofitCallback
import com.proj.memeboard.model.memeApi.MemeApi
import com.proj.memeboard.model.response.LoginResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MemeRepo {
    private val retrofit = Retrofit.Builder().apply {
        baseUrl("https://demo2407529.mockable.io/")
        addConverterFactory(GsonConverterFactory.create())
    }.build()

    private val memeApi = retrofit.create(MemeApi::class.java)

    fun login(loginRequest: LoginRequest?, onDataReceived: (data: Result<User>) -> Unit) {
        if (loginRequest != null) {
            memeApi.login(loginRequest.login, loginRequest.password).enqueue(RetrofitCallback<LoginResponse>(
                { data -> onDataReceived(Result.success(data.convert())) },
                { error -> onDataReceived(Result.failure(error)) }
            ))
        }
        else Result.failure<User>(IllegalArgumentException("input data is null"))
    }

    fun getMemes(onDataReceived: (data: Result<List<MemeResponse>>) -> Unit) {
        memeApi.getMemes().enqueue(RetrofitCallback<List<MemeResponse>> (
            { data -> onDataReceived(Result.success(data)) },
            { error -> onDataReceived(Result.failure(error)) }
        ))
    }
}