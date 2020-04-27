package com.proj.memeboard.ui.main

import android.view.View
import androidx.core.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.repo.MemeRepo
import com.proj.memeboard.ui.Screens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router

@ExperimentalCoroutinesApi
abstract class BaseMemeViewModel(
    private val memeRepo: MemeRepo,
    private val router: Router
) : ViewModel() {

    protected val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun onDetailClick(meme: Meme, cardTransitionOptions: Pair<View, String>) {
        val screen = Screens.DetailScreen(meme, cardTransitionOptions)
        router.navigateTo(screen)
    }

    fun toggleFavorite(meme: Meme) {
        viewModelScope.launch {
            memeRepo.toggleFavorite(meme)
        }
    }
}