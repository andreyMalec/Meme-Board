package com.proj.memeboard.model.memeRepo

import androidx.lifecycle.MutableLiveData
import com.proj.memeboard.domain.User
import com.proj.memeboard.model.LoginRequest
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

    fun login(loginRequest: LoginRequest?): MutableLiveData<Result<User>> {
        val userInfo = MutableLiveData<Result<User>>()

        if (loginRequest != null) {
            memeApi.login(loginRequest.login, loginRequest.password).enqueue(RetrofitCallback<LoginResponse>(
                { data -> userInfo.value = Result.success(data.convert()) },
                { error -> userInfo.value = Result.failure(error) }
            ))
        }
        else Result.failure<User>(IllegalArgumentException("input data is null"))

        return userInfo
    }
}