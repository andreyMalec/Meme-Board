package com.proj.memeboard.ui.main.user

import androidx.lifecycle.*
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.repo.MemeRepo
import com.proj.memeboard.repo.UserRepo
import com.proj.memeboard.service.network.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UserViewModel @Inject constructor(
    private val memeRepo: MemeRepo,
    private val userRepo: UserRepo
) : ViewModel() {

    val memes: LiveData<List<Meme>>

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName

    private val _userDesc = MutableLiveData<String>()
    val userDesc: LiveData<String>
        get() = _userDesc

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isLogout = MutableLiveData(false)
    val isLogout: LiveData<Boolean>
        get() = _isLogout

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
            _isLogout.value = result is Result.Success
            _isLoading.value = false
        }
    }

    fun toggleFavorite(meme: Meme) {
        viewModelScope.launch {
            memeRepo.toggleFavorite(meme)
        }
    }
}