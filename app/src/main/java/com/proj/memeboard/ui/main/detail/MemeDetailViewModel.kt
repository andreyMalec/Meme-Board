package com.proj.memeboard.ui.main.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.localStorage.userStorage.UserStorage
import com.proj.memeboard.service.localDb.repo.DbRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MemeDetailViewModel @Inject constructor(
    private val dbRepo: DbRepo,
    userStorage: UserStorage
) : ViewModel() {

    private val thisAuthor: String

    val isDeleteVisible = MutableLiveData(false)

    init {
        val user = userStorage.getUser()
        thisAuthor = "${user.userName}_${user.firstName}_${user.lastName}"
    }

    fun toggleFavorite(meme: Meme) {
        val updatedMeme = Meme(
            meme.id,
            meme.title,
            meme.description,
            !meme.isFavorite,
            meme.createdDate,
            meme.photoUrl,
            meme.author
        )
        dbRepo.toggleFavorite(viewModelScope, updatedMeme)
    }

    fun setDeleteVisibility(meme: Meme) {
        isDeleteVisible.value = meme.author == thisAuthor
    }

    fun deleteMeme(meme: Meme) {
        dbRepo.deleteMeme(viewModelScope, meme)
    }
}