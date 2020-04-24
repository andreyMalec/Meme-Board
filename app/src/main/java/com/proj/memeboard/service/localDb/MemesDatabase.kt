package com.proj.memeboard.service.localDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.proj.memeboard.domain.Meme

@Database(entities = [Meme::class], version = 1)
abstract class MemesDatabase : RoomDatabase() {
    companion object {
        private lateinit var database: MemesDatabase

        fun instance(context: Context): MemesDatabase {
            if (this::database.isInitialized.not()) {
                synchronized(this) {
                    database = Room.databaseBuilder(
                        context.applicationContext,
                        MemesDatabase::class.java, "memesDb"
                    ).build()
                }
            }
            return database
        }
    }

    abstract fun memesDataDao(): MemesDao
}