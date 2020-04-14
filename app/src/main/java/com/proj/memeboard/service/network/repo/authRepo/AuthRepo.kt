package com.proj.memeboard.service.network.repo.authRepo

import com.proj.memeboard.domain.User
import com.proj.memeboard.service.network.RetrofitCallback
import com.proj.memeboard.service.network.api.authApi.AuthApi
import com.proj.memeboard.service.network.request.LoginRequest
import com.proj.memeboard.service.network.response.LoginResponse

class AuthRepo(private val api: AuthApi) {
    fun login(loginRequest: LoginRequest?, onDataReceived: (data: Result<User>) -> Unit) {
        if (loginRequest != null) {
            api.login(loginRequest).enqueue(RetrofitCallback<LoginResponse>(
                { data -> onDataReceived(Result.success(data.convert())) },
                { error -> onDataReceived(Result.failure(error)) }
            ))
        } else Result.failure<User>(IllegalArgumentException("input data is null"))
    }

    fun logout(onDataReceived: (data: Result<Boolean>) -> Unit) {
        api.logout().enqueue(RetrofitCallback<Boolean>(
            { },
            {
                if (it.message?.contains("null") == true)
                    onDataReceived(Result.success(true))
                else
                    onDataReceived(Result.failure(it))
            }
        ))
    }
}