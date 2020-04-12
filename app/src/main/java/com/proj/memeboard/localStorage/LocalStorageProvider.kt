package com.proj.memeboard.localStorage

import android.content.Context
import android.content.SharedPreferences

object LocalStorageProvider {
    fun create(fromContext: Context, name: String): SharedPreferences =
        fromContext.getSharedPreferences(name, Context.MODE_PRIVATE)
}