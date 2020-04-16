package com.proj.memeboard.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proj.memeboard.domain.User
import com.proj.memeboard.localStorage.userStorage.UserStorage
import com.proj.memeboard.service.network.repo.authRepo.AuthRepo
import com.proj.memeboard.service.network.request.LoginRequest
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val userStorage: UserStorage,
    private val authRepo: AuthRepo
) : ViewModel() {

    private val passwordSize = 8

    val isUserAuthorized = MutableLiveData(checkUserAuthorized())
    val isLoading = MutableLiveData(false)
    val isLoadError = MutableLiveData(false)
    val isLoginInputError = MutableLiveData(false)
    val passInputError = MutableLiveData(-1)

    fun authorizeUser(request: LoginRequest) {
        checkInput(request)

        if (hasInputErrors()) return

        isLoading.value = true
        authRepo.login(request) { user ->
            isLoadError.value =
                user.getOrNull()?.let {
                    saveUserData(it)
                    isUserAuthorized.value = true
                    false
                } ?: true

            isLoading.value = false
        }
    }

    private fun checkInput(request: LoginRequest) {
        checkLoginInput(request.login)
        checkPassInput(request.password)
    }

    fun checkLoginInput(input: String?) {
        isLoginInputError.value = input.isNullOrBlank()
    }

    fun checkPassInput(input: String?) {
        passInputError.value = validatePass(input)
    }

    private fun validatePass(pass: String?): Int {
        return if (pass?.length != passwordSize) {
            if (pass.isNullOrBlank()) 0
            else 1
        } else -1
    }

    private fun hasInputErrors(): Boolean {
        return isLoginInputError.value == true || passInputError.value != -1
    }

    private fun checkUserAuthorized(): Boolean = userStorage.isUserAuthorized()

    private fun saveUserData(user: User) {
        userStorage.setUser(user)
    }
}