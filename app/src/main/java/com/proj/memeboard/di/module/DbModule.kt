package com.proj.memeboard.di.module

import android.content.Context
import androidx.room.Room
import com.proj.memeboard.service.localDb.MemesDao
import com.proj.memeboard.service.localDb.MemesDatabase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module(includes = [ContextModule::class])
class DbModule {
    private lateinit var database: MemesDatabase

    @Provides
    @Singleton
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

    @Provides
    @Singleton
    fun memesDao(context: Context): MemesDao =
        instance(context).memesDataDao()
}