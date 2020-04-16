package com.proj.memeboard.ui.main.detail

import androidx.lifecycle.ViewModel
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.service.localDb.repo.DbRepo
import javax.inject.Inject

class MemeDetailViewModel @Inject constructor(private val dbRepo: DbRepo) : ViewModel() {
    fun toggleFavorite(meme: Meme) {
        dbRepo.toggleFavorite(meme)
    }
}