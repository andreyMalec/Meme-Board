package com.proj.memeboard.repo

import com.proj.memeboard.domain.Meme
import com.proj.memeboard.service.localDb.MemesDao
import com.proj.memeboard.service.network.Result
import com.proj.memeboard.service.network.api.memeApi.MemeApi
import com.proj.memeboard.service.network.apiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MemeRepo(private val api: MemeApi, private val dao: MemesDao) {
    suspend fun loadMemes(): Result<List<Meme>> {
        return apiCall {
            val memes = api.getMemes().map { it.convert() }
            cacheMemes(memes)
            memes
        }
    }

    private suspend fun cacheMemes(memes: List<Meme>) =
        withContext(Dispatchers.IO) {
            dao.insertAll(memes)
        }

    suspend fun toggleFavorite(meme: Meme) =
        withContext(Dispatchers.IO) {
            val updatedMeme = Meme(
                meme.id,
                meme.title,
                meme.description,
                !meme.isFavorite,
                meme.createdDate,
                meme.photoUrl,
                meme.author
            )

            dao.update(updatedMeme)
        }

    fun getAll(): Flow<List<Meme>> = dao.getAll()

    fun getTitleContains(query: String): Flow<List<Meme>> {
        val formattedQuery = "%" + query.trim().toLowerCase() + "%"
        return dao.getTitleContains(formattedQuery)
    }

    fun getCreatedBy(authorId: Long) = dao.getCreatedBy(authorId)

    suspend fun createMeme(meme: Meme) =
        withContext(Dispatchers.IO) {
            dao.insert(meme)
        }

    suspend fun deleteMeme(meme: Meme) =
        withContext(Dispatchers.IO) {
            dao.deleteById(meme.id)
        }
}