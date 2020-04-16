package com.proj.memeboard.ui.main.user

import androidx.lifecycle.*
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.localStorage.userStorage.UserStorage
import com.proj.memeboard.service.localDb.repo.DbRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
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
        memes = dbRepo.getCreatedBy(author).asLiveData()
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
}