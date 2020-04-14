package com.proj.memeboard.ui.main.newMeme

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.localStorage.userStorage.UserStorageProvider
import com.proj.memeboard.service.RepoProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class NewMemeViewModel(private val app: Application) : AndroidViewModel(app) {
    private val userStorage = UserStorageProvider.create(app)
    private val dbRepo = RepoProvider.dbRepo

    val memeImage = MutableLiveData<Bitmap>(null)
    val title = MutableLiveData("")
    val description = MutableLiveData("")
    val canCreate = MutableLiveData(false)

    private fun addMeme(meme: Meme) {
        dbRepo.createMeme(meme)
    }

    fun checkCanCreate() {
        canCreate.value = !title.value.isNullOrBlank() && memeImage.value != null
    }

    fun createMeme() {
        val time = Calendar.getInstance().time.time

        val memeImageFile = File(app.cacheDir, "meme$time.jpg")
        memeImageFile.createNewFile()

        saveMemeImage(memeImageFile)

        val userName: String = userStorage.getUserName()
        val userFirstName: String = userStorage.getFirstName()
        val userLastName: String = userStorage.getLastName()
        val author =  "${userName}_${userFirstName}_${userLastName}"

        addMeme(
            Meme(
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