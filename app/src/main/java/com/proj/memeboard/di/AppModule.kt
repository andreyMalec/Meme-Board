package com.proj.memeboard.di

import com.proj.memeboard.di.activity.ActivityModule
import com.proj.memeboard.di.viewModel.ViewModelModule
import com.proj.memeboard.localStorage.userStorage.UserStorage
import com.proj.memeboard.repo.MemeRepo
import com.proj.memeboard.repo.UserRepo
import com.proj.memeboard.service.localDb.MemesDao
import com.proj.memeboard.service.network.api.authApi.AuthApi
import com.proj.memeboard.service.network.api.memeApi.MemeApi
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module(includes = [ActivityModule::class, ViewModelModule::class])
class AppModule {
    @Provides
    @Singleton
    fun memeRepo(api: MemeApi, dao: MemesDao): MemeRepo = MemeRepo(api, dao)

    @Provides
    @Singleton
    fun authRepo(api: AuthApi, userStorage: UserStorage): UserRepo = UserRepo(api, userStorage)
}