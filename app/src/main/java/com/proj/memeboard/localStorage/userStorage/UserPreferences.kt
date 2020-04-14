package com.proj.memeboard.localStorage.userStorage

enum class UserPreferences(val key: String) {
    USER_PREFERENCES("loginData"),

    TOKEN("token"),
    ID("id"),
    USER_NAME("userName"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    DESC("userDescription")
}