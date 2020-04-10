package com.proj.memeboard.localStorage

import android.content.SharedPreferences

inline operator fun <reified T : Any> SharedPreferences.get(key: String): T {
    return when (T::class) {
        Int::class -> getInt(key, 0) as T
        String::class -> getString(key, "") as T
        Long::class -> getLong(key, 0) as T
        Boolean::class -> getBoolean(key, false) as T

        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}

operator fun SharedPreferences.set(key: String, value: Any) {
    when(value) {
        is Int -> edit { it.putInt(key, value) }
        is String -> edit { it.putString(key, value) }
        is Long -> edit { it.putLong(key, value) }
        is Boolean -> edit { it.putBoolean(key, value) }

        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}

private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}

fun SharedPreferences.clear() {
    edit { it.clear() }
}