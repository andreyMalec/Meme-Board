package com.proj.memeboard.model.memeRepo

import com.proj.memeboard.domain.User
import com.proj.memeboard.model.RetrofitCallback
import com.proj.memeboard.model.memeApi.MemeApi
import com.proj.memeboard.model.request.LoginRequest
import com.proj.memeboard.model.response.LoginResponse
import com.proj.memeboard.model.response.MemeResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MemeRepo(token: String) {
    private val client = OkHttpClient.Builder().addInterceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder().apply {
            header("Authorization", token)
            method(original.method, original.body)
        }.build()
        chain.proceed(request)
    }.build()

    private val retrofit = Retrofit.Builder().apply {
        baseUrl("https://demo2407529.mockable.io/")
        addConverterFactory(GsonConverterFactory.create())
        client(client)
    }.build()

    private val memeApi = retrofit.create(MemeApi::class.java)

    fun login(loginRequest: LoginRequest?, onDataReceived: (data: Result<User>) -> Unit) {
        if (loginRequest != null) {
            memeApi.login(loginRequest.login, loginRequest.password).enqueue(RetrofitCallback<LoginResponse>(
                { data -> onDataReceived(Result.success(data.convert())) },
                { error -> onDataReceived(Result.failure(error)) }
            ))
        } else Result.failure<User>(IllegalArgumentException("input data is null"))
    }

    fun getMemes(onDataReceived: (data: Result<List<MemeResponse>>) -> Unit) {
        memeApi.getMemes().enqueue(RetrofitCallback<List<MemeResponse>>(
            { data -> onDataReceived(Result.success(data)) },
            { error -> onDataReceived(Result.failure(error)) }
        ))
    }
}