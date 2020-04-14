package com.proj.memeboard.service

import android.content.Context
import com.proj.memeboard.service.localDb.MemesDatabase
import com.proj.memeboard.service.localDb.dbRepo.DbRepo
import com.proj.memeboard.localStorage.userStorage.UserStorage
import com.proj.memeboard.localStorage.userStorage.UserStorageProvider
import com.proj.memeboard.service.network.api.authApi.AuthApi
import com.proj.memeboard.service.network.api.memeApi.MemeApi
import com.proj.memeboard.service.network.repo.authRepo.AuthRepo
import com.proj.memeboard.service.network.repo.memeRepo.MemeRepo
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RepoProvider {
    private var userStorage: UserStorage? = null
    private lateinit var db: MemesDatabase

    val memeRepo by lazy { MemeRepo(retrofit.create(MemeApi::class.java)) }
    val authRepo by lazy { AuthRepo(retrofit.create(AuthApi::class.java)) }
    val dbRepo by lazy { DbRepo(db.memesDataDao()) }

    private val client = OkHttpClient.Builder().addInterceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder().apply {
            userStorage?.let {
                header("Authorization", it.getToken())
            }
            method(original.method, original.body)
        }.build()
        chain.proceed(request)
    }.build()

    private val retrofit = Retrofit.Builder().apply {
        baseUrl("https://demo2407529.mockable.io/")
        addConverterFactory(GsonConverterFactory.create())
        client(client)
    }.build()

    fun initUserStorage(fromContext: Context) {
        userStorage = UserStorageProvider.create(fromContext)
        initDao(fromContext)
    }

    private fun initDao(fromContext: Context) {
        db = MemesDatabase.getInstance(fromContext) as MemesDatabase
    }
}