package com.proj.memeboard.di.module

import android.content.Context
import com.proj.memeboard.localStorage.userStorage.UserSharedPrefStorage
import com.proj.memeboard.localStorage.userStorage.UserStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class UserStorageModule {
    @Provides
    @Singleton
    fun userStorage(context: Context): UserStorage = UserSharedPrefStorage(context)
}