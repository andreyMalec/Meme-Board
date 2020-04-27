package com.proj.memeboard.di.module

import com.proj.memeboard.localStorage.userStorage.UserStorage
import com.proj.memeboard.service.network.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module(includes = [UserStorageModule::class])
class OkHttpClientModule {
    @Provides
    @Singleton
    fun okHttpClient(interceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun authorizationInterceptor(userStorage: UserStorage) =
        AuthInterceptor(userStorage)
}