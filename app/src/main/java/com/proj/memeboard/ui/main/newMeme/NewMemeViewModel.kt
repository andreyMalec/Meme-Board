package com.proj.memeboard.ui.main.newMeme

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proj.memeboard.repo.MemeRepo
import com.proj.memeboard.repo.UserRepo
import com.proj.memeboard.util.MemeCreator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.io.File
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
        _canCreate.value = !title.value.isNullOrBlank() && image.value != null &&
                title.value.orEmpty().length <= 140 && description.value.orEmpty().length <= 1000
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

    fun getImageFromResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val fixBitmap: Bitmap? = when {
            resultCode == Activity.RESULT_OK && data != null && data.data != null && requestCode == GALLERY -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, data.data!!)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, data.data)
                }
            }
            resultCode == Activity.RESULT_OK && requestCode == CAMERA -> {
                val newFile = File(context.cacheDir, TEMP_MEME_PATH)

                BitmapFactory.decodeFile(newFile.path)
            }
            else -> null
        }

        image.value = fixBitmap
    }

    fun clearImage() {
        image.value = null
    }

    companion object {
        const val GALLERY = 10
        const val CAMERA = 11

        const val TEMP_MEME_PATH = "myNewMeme.jpg"
    }
}