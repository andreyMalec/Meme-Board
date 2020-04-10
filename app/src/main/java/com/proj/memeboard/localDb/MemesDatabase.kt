package com.proj.memeboard.localDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MemeData::class], version = 1)
abstract class MemesDatabase: RoomDatabase() {
    abstract fun memesDataDao(): MemesDao

    companion object {
        private var database: MemesDatabase? = null

        fun getInstance(context: Context): MemesDatabase? {
            if (database == null) {
                synchronized(MemesDatabase::class) {
                    database = Room.databaseBuilder(
                        context.applicationContext,
                        MemesDatabase::class.java, "memesDb"
                    )
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return database
        }

        fun destroyInstance() {
            database = null
        }
    }
}