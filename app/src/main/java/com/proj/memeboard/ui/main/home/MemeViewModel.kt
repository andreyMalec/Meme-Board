package com.proj.memeboard.ui.main.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.proj.memeboard.localDb.MemeData
import com.proj.memeboard.localStorage.LocalStorageProvider
import com.proj.memeboard.localStorage.UserPreferences
import com.proj.memeboard.localStorage.get
import com.proj.memeboard.model.memeRepo.MemeRepoProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemeViewModel(app: Application) : BaseMemeViewModel(app) {
    private val localStorage = LocalStorageProvider.create(app, UserPreferences.USER_PREFERENCES.key)
    private val repo = MemeRepoProvider.create(localStorage[UserPreferences.TOKEN.key])

    val memes: LiveData<List<MemeData>>
    val searchQuery = MutableLiveData<String>(null)
    val isLoading = MutableLiveData(false)
    val loadError = MutableLiveData(false)

    init {
        memes = Transformations.switchMap(searchQuery) {
            if (it.isNullOrBlank()) {
                dao.getAllLiveData()
            } else {
                val formattedQuery = "%" + it.trim().toLowerCase() + "%"
                dao.getMemesTitleContains(formattedQuery)
            }
        }

        loadMemes()
    }

    fun refreshMemes() {
        searchQuery.value = null
        loadMemes()
    }

    private fun loadMemes() {
        isLoading.value = true

        repo.getMemes { memesResult ->
            loadError.value =
                if (memesResult.isSuccess) {
                    memesResult.getOrNull()?.let { memes ->
                        viewModelScope.launch(Dispatchers.IO) {
                            dao.insertAll(memes.map { it.convert() })
                        }
                        false
                    } ?: true
                } else
                    true

            isLoading.value = false
        }
    }
}