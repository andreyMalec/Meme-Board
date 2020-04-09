package com.proj.memeboard.localStorage

import android.content.Context

object LocalStorageProvider {
    fun create(fromContext: Context): LocalStorage {
        return SharedPreferencesStorage(fromContext)
    }
}