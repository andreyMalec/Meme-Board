package com.proj.memeboard.ui.main.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proj.memeboard.localDb.MemeData
import com.proj.memeboard.localDb.MemesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class BaseMemeViewModel(app: Application) : AndroidViewModel(app) {
    internal val dao = MemesDatabase.getInstance(app)!!.memesDataDao()

    fun updateMeme(meme: MemeData) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(meme)
        }
    }
}