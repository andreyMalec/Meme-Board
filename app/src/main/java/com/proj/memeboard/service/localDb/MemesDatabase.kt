package com.proj.memeboard.service.localDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.proj.memeboard.domain.Meme

@Database(entities = [Meme::class], version = 1)
abstract class MemesDatabase : RoomDatabase() {
    abstract fun memesDataDao(): MemesDao

    companion object {
        private var database: MemesDatabase? = null

        fun getInstance(context: Context): MemesDatabase? {
            if (database == null) {
                synchronized(MemesDatabase::class) {
                    database = Room.databaseBuilder(
                        context.applicationContext,
                        MemesDatabase::class.java, "memesDb"
                    ).build()
                }
            }
            return database
        }

        fun destroyInstance() {
            database = null
        }
    }
}