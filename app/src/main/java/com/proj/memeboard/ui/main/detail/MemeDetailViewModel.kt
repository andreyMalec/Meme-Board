package com.proj.memeboard.ui.main.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.repo.MemeRepo
import com.proj.memeboard.repo.UserRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MemeDetailViewModel @Inject constructor(
    private val memeRepo: MemeRepo,
    private val userRepo: UserRepo
) : ViewModel() {
    val isDeleteVisible = MutableLiveData(false)
    val currentMeme = MutableLiveData<Meme>()

    fun toggleFavorite() {
        viewModelScope.launch {
            currentMeme.value?.let {
                memeRepo.toggleFavorite(it)
            }
        }
    }

    fun setDeleteVisibility() {
        currentMeme.value?.let {
            isDeleteVisible.value = it.author == userRepo.getUser().id
        }
    }

    fun deleteMeme() {
        viewModelScope.launch {
            currentMeme.value?.let {
                memeRepo.deleteMeme(it)
            }
        }
    }
}