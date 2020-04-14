package com.proj.memeboard.localStorage.userStorage

import android.content.Context

object UserStorageProvider {
    fun create(fromContext: Context): UserStorage = UserSharedPrefStorage(fromContext)
}