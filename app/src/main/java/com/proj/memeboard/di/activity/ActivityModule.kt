package com.proj.memeboard.di.activity

import com.proj.memeboard.di.module.DbModule
import com.proj.memeboard.di.module.NetworkModule
import com.proj.memeboard.ui.login.LoginActivity
import com.proj.memeboard.ui.main.MainActivity
import com.proj.memeboard.ui.main.detail.MemeDetailActivity
import com.proj.memeboard.ui.main.newMeme.NewMemeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module(includes = [NetworkModule::class, DbModule::class])
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun contributeDetainActivity(): MemeDetailActivity

    @ContributesAndroidInjector
    abstract fun contributeNewMemeActivity(): NewMemeActivity
}