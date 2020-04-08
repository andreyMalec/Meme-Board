package com.proj.memeboard.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.proj.memeboard.domain.User
import com.proj.memeboard.localStorage.LocalStorageProvider
import com.proj.memeboard.localStorage.UserPreferences
import com.proj.memeboard.model.LoginRequest
import com.proj.memeboard.model.memeRepo.MemeRepoProvider

class LoginViewModel(app: Application): AndroidViewModel(app) {
    private val repo = MemeRepoProvider.memeRepo
    private val localStorage = LocalStorageProvider.create(app.applicationContext)

    val userInputData = MutableLiveData<LoginRequest>()

    val user: LiveData<Result<User>> = Transformations.switchMap(userInputData) {
        repo.login(userInputData.value)
    }

    fun containsUserData(): Boolean {
        return localStorage.contains(UserPreferences.TOKEN)
    }

    fun saveUserData(user: User?) {
        localStorage[UserPreferences.TOKEN] = user?.token ?: ""
        localStorage[UserPreferences.ID] = user?.id ?: -1
        localStorage[UserPreferences.USER_NAME] = user?.userName ?: ""
        localStorage[UserPreferences.FIRST_NAME] = user?.firstName ?: ""
        localStorage[UserPreferences.LAST_NAME] = user?.lastName ?: ""
        localStorage[UserPreferences.DESC] = user?.userDescription ?: ""
    }
}