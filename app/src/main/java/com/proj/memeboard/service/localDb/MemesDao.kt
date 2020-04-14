package com.proj.memeboard.service.localDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import androidx.room.Update
import com.proj.memeboard.domain.Meme

@Dao
interface MemesDao {
    @Query("SELECT * FROM Meme")
    fun getAll(): LiveData<List<Meme>>

    @Query("SELECT * FROM Meme WHERE title LIKE :searchQuery")
    fun getTitleContains(searchQuery: String): LiveData<List<Meme>>

    @Query("SELECT * FROM Meme WHERE author LIKE :author")
    fun getCreatedBy(author: String): LiveData<List<Meme>>

    @Insert(onConflict = IGNORE)
    fun insert(meme: Meme)

    @Insert(onConflict = IGNORE)
    fun insertAll(memes: List<Meme>)

    @Update
    fun update(meme: Meme)

    @Query("DELETE from Meme")
    fun deleteAll()
}