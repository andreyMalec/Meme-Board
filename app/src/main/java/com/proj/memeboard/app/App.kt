package com.proj.memeboard.app

import android.app.Activity
import android.app.Application
import com.proj.memeboard.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        AppInjector.init(this)

        super.onCreate()
    }

    override fun activityInjector() = dispatchingAndroidInjector
}