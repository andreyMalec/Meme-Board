package com.proj.memeboard.model.response

import com.proj.memeboard.localDb.MemeData

data class MemeResponse(
    val id: Long,
    val title: String?,
    val description: String?,
    val isFavorite: Boolean,
    val createdDate: Long,
    val photoUrl: String?
) : BaseResponse<MemeData> {
    override fun convert(): MemeData {
        return MemeData(
            id,
            title,
            description,
            isFavorite,
            createdDate,
            photoUrl
        )
    }
}