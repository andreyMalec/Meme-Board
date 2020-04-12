package com.proj.memeboard.ui.main.newMeme

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.proj.memeboard.localDb.MemeData
import com.proj.memeboard.localDb.MemesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewMemeViewModel(app: Application): AndroidViewModel(app) {
    private val dao = MemesDatabase.getInstance(app.applicationContext)!!.memesDataDao()

    val memeImage = MutableLiveData<Bitmap>(null)
    val title = MutableLiveData("")
    val canCreate = MutableLiveData(false)

    fun addMeme(meme: MemeData) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(meme)
        }
    }

    fun checkCanCreate() {
        canCreate.value = !title.value.isNullOrBlank() && memeImage.value != null
    }
}