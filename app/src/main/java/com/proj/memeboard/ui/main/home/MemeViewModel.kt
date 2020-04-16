package com.proj.memeboard.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.service.localDb.repo.DbRepo
import com.proj.memeboard.service.network.Result
import com.proj.memeboard.service.network.repo.memeRepo.MemeRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

class MemeViewModel @Inject constructor(
    private val dbRepo: DbRepo,
    private val memeRepo: MemeRepo
) : ViewModel() {

    val memes: LiveData<List<Meme>>
    val searchQuery = MutableLiveData<String>(null)
    val isLoading = MutableLiveData(false)
    val isLoadError = MutableLiveData(false)

    init {
        memes = dbRepo.getTitleContainsOrAll(searchQuery)

        loadMemes()
    }

    fun refreshMemes() {
        searchQuery.value = null
        loadMemes()
    }

    fun toggleFavorite(meme: Meme) {
        dbRepo.toggleFavorite(meme)
    }

    private fun loadMemes() {
        isLoading.value = true

        viewModelScope.launch {
            val userResult = memeRepo.getMemes()
            isLoadError.value =
                if (userResult is Result.Success) {
                    dbRepo.cacheMemes(userResult.value)
                    false
                } else true

            isLoading.value = false
        }
    }
}