package com.proj.memeboard.localStorage.userStorage

import android.content.Context
import com.proj.memeboard.domain.User
import com.proj.memeboard.localStorage.clear
import com.proj.memeboard.localStorage.get
import com.proj.memeboard.localStorage.set

class UserSharedPref(context: Context) : UserStorage {
    private val localStorage =
        context.getSharedPreferences(UserPreferences.USER_PREFERENCES.key, Context.MODE_PRIVATE)

    override fun getUser(): User = User(
        getToken(),
        getId(),
        getUserName(),
        getFirstName(),
        getLastName(),
        getUserDescription()
    )

    override fun isUserAuthorized(): Boolean = localStorage.contains(UserPreferences.TOKEN.key)

    override fun getToken(): String = localStorage[UserPreferences.TOKEN.key]

    override fun setToken(value: String) {
        localStorage[UserPreferences.TOKEN.key] = value
    }

    override fun getId(): Long = localStorage[UserPreferences.ID.key]

    override fun setId(value: Long) {
        localStorage[UserPreferences.ID.key] = value
    }

    override fun getUserName(): String = localStorage[UserPreferences.USER_NAME.key]

    override fun setUserName(value: String) {
        localStorage[UserPreferences.USER_NAME.key] = value
    }

    override fun getFirstName(): String = localStorage[UserPreferences.FIRST_NAME.key]

    override fun setFirstName(value: String) {
        localStorage[UserPreferences.FIRST_NAME.key] = value
    }

    override fun getLastName(): String = localStorage[UserPreferences.LAST_NAME.key]

    override fun setLastName(value: String) {
        localStorage[UserPreferences.LAST_NAME.key] = value
    }

    override fun getUserDescription(): String = localStorage[UserPreferences.DESC.key]

    override fun setUserDescription(value: String) {
        localStorage[UserPreferences.DESC.key] = value
    }

    override fun clear() {
        localStorage.clear()
    }
}