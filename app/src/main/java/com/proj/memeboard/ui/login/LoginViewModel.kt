package com.proj.memeboard.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.proj.memeboard.domain.User
import com.proj.memeboard.localStorage.LocalStorageProvider
import com.proj.memeboard.localStorage.UserPreferences
import com.proj.memeboard.localStorage.set
import com.proj.memeboard.model.memeRepo.MemeRepoProvider
import com.proj.memeboard.model.request.LoginRequest

class LoginViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = MemeRepoProvider.memeRepo
    private val localStorage = LocalStorageProvider.create(app.applicationContext, UserPreferences.USER_PREFERENCES.key)

    private val passwordSize = 8

    val userAuthorized = MutableLiveData(isUserAuthorized())
    val isLoading = MutableLiveData(false)
    val loadError = MutableLiveData(false)
    val loginInputError = MutableLiveData(false)
    val passInputError = MutableLiveData(-1)

    fun authorizeUser(request: LoginRequest) {
        checkInput(request)

        if (hasInputErrors()) return

        isLoading.value = true
        repo.login(request) { userResult ->
            if (userResult.isSuccess) {
                loadError.value = false
                saveUserData(userResult.getOrNull())
                userAuthorized.value = true
            } else
                loadError.value = true

            isLoading.value = false
        }
    }

    private fun checkInput(request: LoginRequest) {
        checkLoginInput(request.login)
        checkPassInput(request.password)
    }

    fun checkLoginInput(input: String?) {
        loginInputError.value = input.isNullOrBlank()
    }

    fun checkPassInput(input: String?) {
        passInputError.value = if (input?.length != passwordSize) {
            if (input.isNullOrBlank()) 0
            else 1
        } else -1
    }

    private fun hasInputErrors(): Boolean {
        return loginInputError.value == true || passInputError.value != -1
    }

    private fun isUserAuthorized(): Boolean {
        return localStorage.contains(UserPreferences.TOKEN.key)
    }

    private fun saveUserData(user: User?) {
        localStorage[UserPreferences.TOKEN.key] = user?.token ?: ""
        localStorage[UserPreferences.ID.key] = user?.id ?: -1
        localStorage[UserPreferences.USER_NAME.key] = user?.userName ?: "userName"
        localStorage[UserPreferences.FIRST_NAME.key] = user?.firstName ?: "firstName"
        localStorage[UserPreferences.LAST_NAME.key] = user?.lastName ?: "lastName"
        localStorage[UserPreferences.DESC.key] = user?.userDescription ?: "description"
    }
}