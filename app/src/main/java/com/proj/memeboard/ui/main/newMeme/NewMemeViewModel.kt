package com.proj.memeboard.ui.main.newMeme

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proj.memeboard.repo.MemeRepo
import com.proj.memeboard.repo.UserRepo
import com.proj.memeboard.util.MemeCreator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class NewMemeViewModel @Inject constructor(
    private val context: Context,
    private val userRepo: UserRepo,
    private val memeRepo: MemeRepo
) : ViewModel() {

    val image = MutableLiveData<Bitmap>(null)
    val title = MutableLiveData("")
    val description = MutableLiveData("")

    private val _canCreate = MutableLiveData(false)
    val canCreate: LiveData<Boolean>
        get() = _canCreate

    fun checkCanCreate() {
        _canCreate.value = !title.value.isNullOrBlank() && image.value != null
    }

    fun createMeme() {
        if (_canCreate.value == true) {
            viewModelScope.launch {
                val newMeme = MemeCreator(context, userRepo.getUser().id).create(
                    title.value!!,//мы уже проверили
                    description.value,
                    image.value!!
                )
                memeRepo.createMeme(newMeme)
            }

            image.value = null
            title.value = null
        }
    }

    companion object {
        const val GALLERY = 10
        const val CAMERA = 11

        const val TEMP_MEME_PATH = "myNewMeme.jpg"
    }
}