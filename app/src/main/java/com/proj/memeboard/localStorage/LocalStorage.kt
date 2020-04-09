package com.proj.memeboard.localStorage

interface LocalStorage {
    fun getString(pref: UserPreferences): String

    fun getInt(pref: UserPreferences): Int

    fun getLong(pref: UserPreferences): Long

    operator fun set(pref: UserPreferences, value: String)

    operator fun set(pref: UserPreferences, value: Int)

    operator fun set(pref: UserPreferences, value: Long)

    operator fun contains(pref: UserPreferences): Boolean

    fun clear()
}