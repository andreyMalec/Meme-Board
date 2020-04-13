package com.proj.memeboard.ui.main.newMeme

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.proj.memeboard.localDb.MemeData
import com.proj.memeboard.localDb.MemesDatabase
import com.proj.memeboard.localStorage.LocalStorageProvider
import com.proj.memeboard.localStorage.UserPreferences
import com.proj.memeboard.localStorage.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class NewMemeViewModel(private val app: Application) : AndroidViewModel(app) {
    private val dao = MemesDatabase.getInstance(app)!!.memesDataDao()
    private val localStorage = LocalStorageProvider.create(app, UserPreferences.USER_PREFERENCES.key)

    val memeImage = MutableLiveData<Bitmap>(null)
    val title = MutableLiveData("")
    val description = MutableLiveData("")
    val canCreate = MutableLiveData(false)

    private fun addMeme(meme: MemeData) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(meme)
        }
    }

    fun checkCanCreate() {
        canCreate.value = !title.value.isNullOrBlank() && memeImage.value != null
    }

    fun createMeme() {
        val time = Calendar.getInstance().time.time

        val memeImageFile = File(app.cacheDir, "meme$time.jpg")
        memeImageFile.createNewFile()

        saveMemeImage(memeImageFile)

        val userName: String = localStorage[UserPreferences.USER_NAME.key]
        val userFirstName: String = localStorage[UserPreferences.FIRST_NAME.key]
        val userLastName: String = localStorage[UserPreferences.LAST_NAME.key]
        val author =  "${userName}_${userFirstName}_${userLastName}"

        addMeme(
            MemeData(
                id = time,
                title = title.value,
                description = description.value,
                isFavorite = true,
                createdDate = time / 1000,
                photoUrl = memeImageFile.absolutePath,
                author = author
            )
        )

        memeImage.value = null
        title.value = null
    }

    private fun saveMemeImage(memeImageFile: File) {
        val bos = ByteArrayOutputStream()
        memeImage.value?.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmapData = bos.toByteArray()

        FileOutputStream(memeImageFile).apply {
            write(bitmapData)
            flush()
        }
    }

    companion object {
        const val GALLERY = 10
        const val CAMERA = 11

        const val TEMP_MEME_PATH = "myNewMeme.jpg"
    }
}