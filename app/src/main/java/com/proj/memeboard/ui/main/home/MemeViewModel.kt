package com.proj.memeboard.ui.main.home

import androidx.lifecycle.*
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.service.localDb.repo.DbRepo
import com.proj.memeboard.service.network.Result
import com.proj.memeboard.service.network.repo.memeRepo.MemeRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MemeViewModel @Inject constructor(
    private val dbRepo: DbRepo,
    private val memeRepo: MemeRepo
) : ViewModel() {

    val memes: LiveData<List<Meme>>
    val searchQuery = MutableLiveData<String>(null)
    val isLoading = MutableLiveData(false)
    val isLoadError = MutableLiveData(false)

    init {
        memes = searchQuery.asFlow().flatMapLatest { query ->
            if (query.isNullOrBlank())
                dbRepo.getAll()
            else
                dbRepo.getTitleContains(query)
        }.asLiveData()

        loadMemes()
    }

    fun refreshMemes() {
        searchQuery.value = null
        loadMemes()
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

    private fun loadMemes() {
        isLoading.value = true

        viewModelScope.launch {
            val userResult = memeRepo.getMemes()
            isLoadError.value =
                if (userResult is Result.Success) {
                    dbRepo.cacheMemes(this, userResult.value)
                    false
                } else true

            isLoading.value = false
        }
    }
}