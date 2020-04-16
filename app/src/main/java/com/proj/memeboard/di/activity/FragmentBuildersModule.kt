package com.proj.memeboard.di.activity

import com.proj.memeboard.ui.main.home.MemeFragment
import com.proj.memeboard.ui.main.newMeme.NewMemeFragment
import com.proj.memeboard.ui.main.user.UserFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeMemeFragment(): MemeFragment

    @ContributesAndroidInjector
    abstract fun contributeNewMemeFragment(): NewMemeFragment

    @ContributesAndroidInjector
    abstract fun contributeUserFragment(): UserFragment
}