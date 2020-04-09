package com.proj.memeboard.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.model.memeRepo.MemeRepoProvider

class MemeViewModel: ViewModel() {
    private val repo = MemeRepoProvider.memeRepo

    val refreshMemes = MutableLiveData(false)

    val memes: LiveData<Result<List<Meme>>> = Transformations.switchMap(refreshMemes) {
        repo.getMemes()
    }
}