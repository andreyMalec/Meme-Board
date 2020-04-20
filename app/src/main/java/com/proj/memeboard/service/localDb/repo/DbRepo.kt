package com.proj.memeboard.service.localDb.repo

import com.proj.memeboard.domain.Meme
import com.proj.memeboard.service.localDb.MemesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class DbRepo(private val dao: MemesDao) {
    fun toggleFavorite(scope: CoroutineScope, meme: Meme) {
        scope.launch(Dispatchers.IO) {
            dao.update(meme)
        }
    }

    fun getAll(): Flow<List<Meme>> = dao.getAll()

    fun getTitleContains(query: String): Flow<List<Meme>> {
        val formattedQuery = "%" + query.trim().toLowerCase() + "%"
        return dao.getTitleContains(formattedQuery)
    }

    fun getCreatedBy(author: String): Flow<List<Meme>> = dao.getCreatedBy(author)

    fun cacheMemes(scope: CoroutineScope, memes: List<Meme>) {
        scope.launch(Dispatchers.IO) {
            dao.insertAll(memes)
        }
    }

    fun createMeme(scope: CoroutineScope, meme: Meme) {
        scope.launch(Dispatchers.IO) {
            dao.insert(meme)
        }
    }

    fun deleteMeme(scope: CoroutineScope, meme: Meme) {
        scope.launch(Dispatchers.IO) {
            dao.deleteById(meme.id)
        }
    }
}