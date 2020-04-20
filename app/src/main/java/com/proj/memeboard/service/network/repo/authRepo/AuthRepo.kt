package com.proj.memeboard.service.network.repo.authRepo

import com.proj.memeboard.domain.User
import com.proj.memeboard.service.network.Result
import com.proj.memeboard.service.network.api.authApi.AuthApi
import com.proj.memeboard.service.network.apiCall
import com.proj.memeboard.service.network.request.LoginRequest
import kotlinx.coroutines.Dispatchers

class AuthRepo(private val api: AuthApi) {
    suspend fun login(loginRequest: LoginRequest?): Result<User> {
        return loginRequest?.let {
            apiCall(Dispatchers.IO) { api.login(it).convert() }

        } ?: Result.Failure(0, "Input data is null")
    }

    suspend fun logout(): Result<Nothing> {
        return apiCall(Dispatchers.IO) { api.logout() }
    }
}