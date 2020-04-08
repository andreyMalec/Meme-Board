package com.proj.memeboard.model.response

import com.proj.memeboard.domain.User
import com.proj.memeboard.model.UserInfo

data class LoginResponse(val accessToken: String, val userInfo: UserInfo?): BaseResponse<User> {
    override fun convert(): User {
        return User(
            accessToken,
            userInfo?.id ?: -1,
            userInfo?.userName ?: "",
            userInfo?.firstName ?: "",
            userInfo?.lastName ?: "",
            userInfo?.userDescription ?: ""
        )
    }
}