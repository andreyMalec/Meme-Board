package com.proj.memeboard.ui.main.home

import androidx.lifecycle.*
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.repo.MemeRepo
import com.proj.memeboard.service.network.Result
import com.proj.memeboard.ui.Screens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MemeViewModel @Inject constructor(
    private val memeRepo: MemeRepo,
    private val router: Router
) : ViewModel() {

    val memes: LiveData<List<Meme>>
    val searchQuery = MutableLiveData<String>(null)

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isLoadError = MutableLiveData(false)
    val isLoadError: LiveData<Boolean>
        get() = _isLoadError

    init {
        memes = searchQuery.asFlow().flatMapLatest { query ->
            if (query.isNullOrBlank())
                memeRepo.getAll()
            else
                memeRepo.getTitleContains(query)
        }.asLiveData()

        loadMemes()
    }

    fun refreshMemes() {
        searchQuery.value = null
        loadMemes()
    }

    fun toggleFavorite(meme: Meme) {
        viewModelScope.launch {
            memeRepo.toggleFavorite(meme)
        }
    }

    fun onDetailClick(meme: Meme) {
        router.navigateTo(Screens.DetailScreen(meme))
    }

    private fun loadMemes() {
        _isLoading.value = true

        viewModelScope.launch {
            val userResult = memeRepo.loadMemes()
            _isLoadError.value = userResult !is Result.Success

            _isLoading.value = false
        }
    }
}