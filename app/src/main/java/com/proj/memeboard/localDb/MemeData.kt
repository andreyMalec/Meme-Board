package com.proj.memeboard.localDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MemeData(
    @PrimaryKey
    val id: Long,
    val title: String?,
    val description: String?,
    val isFavorite: Boolean,
    val createdDate: Long,
    val photoUrl: String?
)