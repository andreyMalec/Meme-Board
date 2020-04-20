package com.proj.memeboard.di.module

import com.proj.memeboard.localStorage.userStorage.UserStorage
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module(includes = [UserStorageModule::class])
class OkHttpClientModule {
    @Provides
    @Singleton
    fun okHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun interceptor(userStorage: UserStorage): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder().apply {
                header("Authorization", userStorage.getToken())
                method(original.method, original.body)
            }.build()
            chain.proceed(request)
        }
    }
}