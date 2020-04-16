package com.proj.memeboard.service.localDb.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.service.localDb.MemesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DbRepo(private val dao: MemesDao) {
    fun toggleFavorite(meme: Meme) {
        GlobalScope.launch(Dispatchers.IO) {
            dao.update(meme)
        }
    }

    fun getTitleContainsOrAll(query: LiveData<String>): LiveData<List<Meme>> {
        return Transformations.switchMap(query) {
            if (it.isNullOrBlank())
                getAll()
            else
                getTitleContains(it)
        }
    }

    private fun getAll(): LiveData<List<Meme>> = dao.getAll()

    private fun getTitleContains(query: String): LiveData<List<Meme>> {
        val formattedQuery = "%" + query.trim().toLowerCase() + "%"
        return dao.getTitleContains(formattedQuery)
    }

    fun getCreatedBy(author: String): LiveData<List<Meme>> = dao.getCreatedBy(author)

    fun cacheMemes(memes: List<Meme>) {
        GlobalScope.launch(Dispatchers.IO) {
            dao.insertAll(memes)
        }
    }

    fun createMeme(meme: Meme) {
        GlobalScope.launch(Dispatchers.IO) {
            dao.insert(meme)
        }
    }
}