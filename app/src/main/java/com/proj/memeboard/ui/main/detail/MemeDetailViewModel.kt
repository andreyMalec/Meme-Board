package com.proj.memeboard.ui.main.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.service.localDb.repo.DbRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MemeDetailViewModel @Inject constructor(private val dbRepo: DbRepo) : ViewModel() {
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