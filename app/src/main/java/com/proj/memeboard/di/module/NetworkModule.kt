package com.proj.memeboard.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.proj.memeboard.service.network.api.authApi.AuthApi
import com.proj.memeboard.service.network.api.memeApi.MemeApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [OkHttpClientModule::class])
class NetworkModule {
    @Provides
    @Singleton
    fun memeApi(retrofit: Retrofit): MemeApi = retrofit.create(MemeApi::class.java)

    @Provides
    @Singleton
    fun authApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl("https://demo2407529.mockable.io/")
            addConverterFactory(gsonConverterFactory)
            client(okHttpClient)
        }.build()
    }

    @Provides
    @Singleton
    fun gson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun gsonConverterFactory(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)
}