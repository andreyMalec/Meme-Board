package com.proj.memeboard.localStorage.userStorage

import com.proj.memeboard.domain.User

interface UserStorage {
    fun setUser(user: User)
    fun getUser(): User

    fun isUserAuthorized(): Boolean

    fun getToken(): String
    fun setToken(value: String)

    fun getId(): Long
    fun setId(value: Long)

    fun getUserName(): String
    fun setUserName(value: String)

    fun getFirstName(): String
    fun setFirstName(value: String)

    fun getLastName(): String
    fun setLastName(value: String)

    fun getUserDescription(): String
    fun setUserDescription(value: String)

    fun clear()
}