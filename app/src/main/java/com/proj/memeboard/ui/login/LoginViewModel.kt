package com.proj.memeboard.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proj.memeboard.repo.UserRepo
import com.proj.memeboard.service.network.Result
import com.proj.memeboard.ui.Screens
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val userRepo: UserRepo, private val router: Router) : ViewModel() {

    private val passwordSize = 8

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isLoadError = MutableLiveData(false)
    val isLoadError: LiveData<Boolean>
        get() = _isLoadError

    private val _isLoginInputError = MutableLiveData(false)
    val isLoginInputError: LiveData<Boolean>
        get() = _isLoginInputError

    private val _passInputError = MutableLiveData(-1)
    val passInputError: LiveData<Int>
        get() = _passInputError

    init {
        if (userRepo.isUserAuthorized())
            startMainScreen()
    }

    private fun startMainScreen() {
        router.newRootScreen(Screens.MainScreen)
    }

    fun authorizeUser(login: String, pass: String) {
        checkInput(login, pass)

        if (hasInputErrors()) return

        _isLoading.value = true
        viewModelScope.launch {
            val userResult = userRepo.login(login, pass)
            _isLoadError.value = userResult !is Result.Success

            if (userRepo.isUserAuthorized())
                startMainScreen()

            _isLoading.value = false
        }
    }

    private fun checkInput(login: String, pass: String) {
        checkLoginInput(login)
        checkPassInput(pass)
    }

    fun checkLoginInput(input: String?) {
        _isLoginInputError.value = input.isNullOrBlank()
    }

    fun checkPassInput(input: String?) {
        _passInputError.value = validatePass(input)
    }

    private fun validatePass(pass: String?): Int {
        return if (pass?.length != passwordSize) {
            if (pass.isNullOrBlank()) 0
            else 1
        } else -1
    }

    private fun hasInputErrors(): Boolean {
        return _isLoginInputError.value == true || _passInputError.value != -1
    }
}