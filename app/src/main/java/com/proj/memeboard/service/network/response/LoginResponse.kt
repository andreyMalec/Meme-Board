package com.proj.memeboard.service.network.response

import com.google.gson.annotations.SerializedName
import com.proj.memeboard.domain.User

data class LoginResponse(
    @SerializedName("accessToken")
    val accessToken: String?,
    @SerializedName("userInfo")
    val userInfo: UserInfo?
) : BaseResponse<User> {
    override fun convert(): User {
        return User(
            accessToken ?: "",
            userInfo?.id ?: -1,
            userInfo?.userName ?: "",
            userInfo?.firstName ?: "",
            userInfo?.lastName ?: "",
            userInfo?.userDescription ?: ""
        )
    }

    inner class UserInfo(
        @SerializedName("id")
        val id: Long?,
        @SerializedName("userName")
        val userName: String?,
        @SerializedName("firstName")
        val firstName: String?,
        @SerializedName("lastName")
        val lastName: String?,
        @SerializedName("userDescription")
        val userDescription: String?
    )
}