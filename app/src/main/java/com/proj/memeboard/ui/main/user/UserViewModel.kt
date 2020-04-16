package com.proj.memeboard.ui.main.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.localStorage.userStorage.UserStorage
import com.proj.memeboard.service.localDb.repo.DbRepo
import javax.inject.Inject

class UserViewModel @Inject constructor(
    userStorage: UserStorage,
    private val dbRepo: DbRepo
) : ViewModel() {
    val memes: LiveData<List<Meme>>?
    val isLoading = MutableLiveData(true)
    val loadError = MutableLiveData(false)

    init {
        val user = userStorage.getUser()
        val author = "${user.userName}_${user.firstName}_${user.lastName}"
        memes = dbRepo.getCreatedBy(author)
    }

    fun toggleFavorite(meme: Meme) {
        dbRepo.toggleFavorite(meme)
    }
}