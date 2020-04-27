package com.proj.memeboard.ui.main.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.repo.MemeRepo
import com.proj.memeboard.repo.UserRepo
import com.proj.memeboard.service.network.Result
import com.proj.memeboard.ui.Screens
import com.proj.memeboard.ui.main.BaseMemeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UserViewModel @Inject constructor(
    private val memeRepo: MemeRepo,
    private val userRepo: UserRepo,
    private val router: Router
) : BaseMemeViewModel(memeRepo, router) {

    val memes: LiveData<List<Meme>>

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName

    private val _userDesc = MutableLiveData<String>()
    val userDesc: LiveData<String>
        get() = _userDesc

    init {
        val user = userRepo.getUser()
        memes = memeRepo.getCreatedBy(user.id).asLiveData()

        _userName.value = user.firstName
        _userDesc.value = user.userDescription

        _isLoading.value = false
    }

    fun logout() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = userRepo.logout()
            if (result is Result.Success)
                router.newRootScreen(Screens.LoginScreen)
            _isLoading.value = false
        }
    }
}