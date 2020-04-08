package com.proj.memeboard.localStorage

interface LocalStorage {
    fun getString(pref: UserPreferences): String

    fun getInt(pref: UserPreferences): Int

    operator fun set(pref: UserPreferences, value: String)

    operator fun set(pref: UserPreferences, value: Int)

    operator fun contains(pref: UserPreferences): Boolean

    fun clear()
}