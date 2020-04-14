package com.proj.memeboard.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.service.RepoProvider

open class BaseMemeViewModel(app: Application) : AndroidViewModel(app) {
    internal val dbRepo = RepoProvider.dbRepo

    fun toggleFavorite(meme: Meme) {
        dbRepo.toggleFavorite(meme)
    }
}