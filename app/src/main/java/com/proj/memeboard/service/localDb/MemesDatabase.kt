package com.proj.memeboard.service.localDb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.proj.memeboard.domain.Meme

@Database(entities = [Meme::class], version = 1)
abstract class MemesDatabase : RoomDatabase() {
    abstract fun memesDataDao(): MemesDao
}