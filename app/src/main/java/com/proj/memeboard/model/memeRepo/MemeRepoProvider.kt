package com.proj.memeboard.model.memeRepo

object MemeRepoProvider {
    fun create(token: String): MemeRepo {
        return MemeRepo(token)
    }
}