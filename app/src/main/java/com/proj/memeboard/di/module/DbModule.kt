package com.proj.memeboard.di.module

import android.content.Context
import androidx.room.Room
import com.proj.memeboard.service.localDb.MemesDatabase
import com.proj.memeboard.service.localDb.repo.DbRepo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class DbModule {
    private lateinit var database: MemesDatabase

    @Provides
    @Singleton
    fun instance(context: Context): MemesDatabase {
        if (this::database.isInitialized.not()) {
            synchronized(DbModule::class) {
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
    fun dbRepo(context: Context): DbRepo = DbRepo(instance(context).memesDataDao())
}