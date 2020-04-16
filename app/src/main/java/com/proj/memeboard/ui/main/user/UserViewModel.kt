package com.proj.memeboard.ui.main.user

import androidx.lifecycle.*
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.localStorage.userStorage.UserStorage
import com.proj.memeboard.service.localDb.repo.DbRepo
import com.proj.memeboard.service.network.repo.authRepo.AuthRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.proj.memeboard.service.network.Result

@ExperimentalCoroutinesApi
class UserViewModel @Inject constructor(
    private val userStorage: UserStorage,
    private val dbRepo: DbRepo,
    private val authRepo: AuthRepo
) : ViewModel() {

    val memes: LiveData<List<Meme>>?
    val userName = MutableLiveData<String>()
    val userDesc =  MutableLiveData<String>()
    val isLoading = MutableLiveData(true)
    val isLoadError = MutableLiveData(false)
    val isLogout = MutableLiveData(false)

    init {
        val user = userStorage.getUser()
        val author = "${user.userName}_${user.firstName}_${user.lastName}"
        memes = dbRepo.getCreatedBy(author).asLiveData()

        userName.value = user.firstName
        userDesc.value = user.userDescription
    }

    fun logout() {
        isLoading.value = true
        viewModelScope.launch {
            val result = authRepo.logout()
            isLogout.value =
                if (result is Result.Failure && result.code == 204) {
                    userStorage.clear()
                    true
                } else false

            isLoading.value = false
        }
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