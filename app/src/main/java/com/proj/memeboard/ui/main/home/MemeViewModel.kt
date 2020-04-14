package com.proj.memeboard.ui.main.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.service.RepoProvider
import com.proj.memeboard.ui.main.BaseMemeViewModel

class MemeViewModel(app: Application) : BaseMemeViewModel(app) {
    private val memeRepo = RepoProvider.memeRepo

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

    private fun loadMemes() {
        isLoading.value = true

        memeRepo.getMemes { memes ->
            isLoadError.value =
                memes.getOrNull()?.let {
                    dbRepo.cacheMemes(it)
                    false
                } ?: true

            isLoading.value = false
        }
    }
}