package com.proj.memeboard.di.module

import android.content.Context
import com.proj.memeboard.service.localDb.MemesDao
import com.proj.memeboard.service.localDb.MemesDatabase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module(includes = [ContextModule::class])
class DbModule {

    @Provides
    @Singleton
    fun instance(context: Context): MemesDatabase = MemesDatabase.instance(context)

    @Provides
    @Singleton
    fun memesDao(db: MemesDatabase): MemesDao = db.memesDataDao()
}