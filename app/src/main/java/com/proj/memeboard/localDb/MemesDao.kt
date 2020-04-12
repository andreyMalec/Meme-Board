package com.proj.memeboard.localDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import androidx.room.Update

@Dao
interface MemesDao {
    @Query("SELECT * FROM MemeData")
    fun getAllLiveData(): LiveData<List<MemeData>>

    @Query("SELECT * FROM MemeData")
    fun getAll(): List<MemeData>

    @Insert(onConflict = IGNORE)
    fun insert(memeData: MemeData)

    @Insert(onConflict = IGNORE)
    fun insertAll(memes: List<MemeData>)

    @Update
    fun update(memeData: MemeData)

    @Query("SELECT * FROM MemeData WHERE title LIKE :searchQuery")
    fun getMemesTitleContains(searchQuery: String): LiveData<List<MemeData>>

    @Query("DELETE from MemeData")
    fun deleteAll()

    fun isEmpty(): Boolean = getAll().isEmpty()
}