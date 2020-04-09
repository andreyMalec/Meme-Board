package com.proj.memeboard.localStorage

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesStorage(context: Context): LocalStorage {
    private val preferences: SharedPreferences = context.getSharedPreferences(UserPreferences.USER_PREFERENCES.key, Context.MODE_PRIVATE)

    override fun getString(pref: UserPreferences): String  {
        return preferences.getString(pref.key, "") ?: ""
    }

    override fun getInt(pref: UserPreferences): Int  {
        return preferences.getInt(pref.key, 0)
    }

    override operator fun set(pref: UserPreferences, value: String) {
        preferences.edit().putString(pref.key, value).apply()
    }

    override operator fun set(pref: UserPreferences, value: Int) {
        preferences.edit().putInt(pref.key, value).apply()
    }

    override fun contains(pref: UserPreferences): Boolean {
        return preferences.contains(pref.key)
    }

    override fun clear() {
        preferences.edit().clear().apply()
    }
}