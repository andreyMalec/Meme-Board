package com.proj.memeboard.localDb

import androidx.room.Dao
import androidx.room.Query
import androidx.paging.DataSource
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface MemesDao {
    @Query("SELECT * FROM MemeData")
    fun factory(): DataSource.Factory<Int, MemeData>

    @Query("SELECT * FROM MemeData")
    fun getAll(): List<MemeData>

    @Insert(onConflict = REPLACE)
    fun insert(memeData: MemeData)

    @Insert(onConflict = REPLACE)
    fun insertAll(memes: List<MemeData>)

    @Query("DELETE from MemeData")
    fun deleteAll()

    fun isEmpty(): Boolean = getAll().isEmpty()
}