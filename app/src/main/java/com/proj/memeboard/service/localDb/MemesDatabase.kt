package com.proj.memeboard.service.localDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.proj.memeboard.domain.Meme

@Database(entities = [Meme::class], version = 1)
abstract class MemesDatabase : RoomDatabase() {
    companion object {
        fun instance(context: Context): MemesDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MemesDatabase::class.java, "memesDb"
            ).build()
        }
    }

    abstract fun memesDataDao(): MemesDao
}