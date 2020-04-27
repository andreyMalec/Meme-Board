package com.proj.memeboard.repo

import com.proj.memeboard.domain.User
import com.proj.memeboard.localStorage.userStorage.UserStorage
import com.proj.memeboard.service.network.Result
import com.proj.memeboard.service.network.api.authApi.AuthApi
import com.proj.memeboard.service.network.apiCall
import com.proj.memeboard.service.network.request.LoginRequest

class UserRepo(private val api: AuthApi, private val userStorage: UserStorage) {
    suspend fun login(login: String, pass: String): Result<User> {
        val request = LoginRequest(login, pass)
        return apiCall {
            val user = api.login(request).convert()
            saveUser(user)
            user
        }
    }

    suspend fun logout(): Result<Unit> {
        return apiCall {
            api.logout()
            userStorage.clear()
        }
    }

    fun isUserAuthorized() = userStorage.isUserAuthorized()

    fun getUser() = userStorage.getUser()

    private fun saveUser(user: User) {
        userStorage.apply {
            setToken(user.token)
            setId(user.id)
            setUserName(user.userName)
            setFirstName(user.firstName)
            setLastName(user.lastName)
            setUserDescription(user.userDescription)
        }
    }
}