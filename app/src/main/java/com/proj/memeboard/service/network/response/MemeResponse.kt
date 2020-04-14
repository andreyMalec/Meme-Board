package com.proj.memeboard.service.network.response

import com.google.gson.annotations.SerializedName
import com.proj.memeboard.domain.Meme

data class MemeResponse(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("isFavorite")
    val isFavorite: Boolean?,
    @SerializedName("createdDate")
    val createdDate: Long?,
    @SerializedName("photoUrl")
    val photoUrl: String?
) : BaseResponse<Meme> {
    override fun convert(): Meme {
        return Meme(
            id ?: 0,
            title,
            description,
            isFavorite ?: false,
            createdDate ?: 0,
            photoUrl,
            null
        )
    }
}