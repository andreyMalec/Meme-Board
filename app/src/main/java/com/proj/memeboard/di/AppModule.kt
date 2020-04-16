package com.proj.memeboard.di

import com.proj.memeboard.di.activity.ActivityModule
import com.proj.memeboard.di.viewModel.ViewModelModule
import dagger.Module

@Module(includes = [ActivityModule::class, ViewModelModule::class])
class AppModule