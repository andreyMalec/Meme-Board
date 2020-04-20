package com.proj.memeboard.service.localDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import androidx.room.Update
import com.proj.memeboard.domain.Meme
import kotlinx.coroutines.flow.Flow

@Dao
interface MemesDao {
    @Query("SELECT * FROM Meme")
    fun getAll(): Flow<List<Meme>>

    @Query("SELECT * FROM Meme WHERE title LIKE :searchQuery")
    fun getTitleContains(searchQuery: String): Flow<List<Meme>>

    @Query("SELECT * FROM Meme WHERE author LIKE :author")
    fun getCreatedBy(author: String): Flow<List<Meme>>

    @Insert(onConflict = IGNORE)
    fun insert(meme: Meme)

    @Insert(onConflict = IGNORE)
    fun insertAll(memes: List<Meme>)

    @Update
    fun update(meme: Meme)

    @Query("DELETE from Meme")
    fun deleteAll()

    @Query("DELETE from Meme WHERE id = :id")
    fun deleteById(id: Long)
}