package com.proj.memeboard.ui.login

import android.app.Application
import androidx.lifecycle.*
import com.proj.memeboard.domain.User
import com.proj.memeboard.localStorage.LocalStorageProvider
import com.proj.memeboard.localStorage.UserPreferences
import com.proj.memeboard.model.request.LoginRequest
import com.proj.memeboard.model.memeRepo.MemeRepoProvider
import com.proj.memeboard.localStorage.set

class LoginViewModel(app: Application): AndroidViewModel(app) {
    private val repo = MemeRepoProvider.memeRepo
    private val localStorage = LocalStorageProvider.create(app.applicationContext, UserPreferences.USER_PREFERENCES.key)

    val userInputData = MutableLiveData<LoginRequest>()
    val userResult: LiveData<LoginRequest>

    val userAuthorized = MutableLiveData(isUserAuthorized())
    val isLoading = MutableLiveData(false)
    val loginError = MutableLiveData(false)

    init {
        userResult = Transformations.switchMap(userInputData) { value ->
            isLoading.value = true
            repo.login(value) { userResult ->
                if (userResult.isSuccess) {
                    loginError.value = false
                    saveUserData(userResult.getOrNull())
                    userAuthorized.value = true
                } else
                    loginError.value = true

                isLoading.value = false
            }
            MutableLiveData<LoginRequest>()
        }
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