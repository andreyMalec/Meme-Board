package com.proj.memeboard.domain

data class User(
    val token: String,
    val id: Int,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val userDescription: String
)