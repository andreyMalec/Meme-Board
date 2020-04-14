package com.proj.memeboard.ui.main.user

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.localStorage.userStorage.UserSharedPrefStorage
import com.proj.memeboard.ui.main.BaseMemeViewModel

class UserViewModel(app: Application) : BaseMemeViewModel(app) {
    private val userStorage = UserSharedPrefStorage(app)

    val memes: LiveData<List<Meme>>?
    val isLoading = MutableLiveData(true)
    val loadError = MutableLiveData(false)

    init {
        val user = userStorage.getUser()
        val author =  "${user.userName}_${user.firstName}_${user.lastName}"
        memes = dbRepo.getCreatedBy(author)
    }
}