package com.example.sharkflow.data.api.dto.board

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class BoardDto(
    @SerializedName("uuid") val uuid: String,
    @SerializedName("serverUuid") val serverUuid: String?,
    @SerializedName("title") val title: String,
    @SerializedName("color") val color: String? = "FFFFFF",
    @SerializedName("userUuid") val userUuid: String,
    @SerializedName("createdAt") val createdAt: Instant?,
    @SerializedName("updatedAt") val updatedAt: Instant?,
    @SerializedName("isPinned") val isPinned: Boolean? = false,
    @SerializedName("isFavorite") val isFavorite: Boolean? = false,
    @SerializedName("isDeleted") val isDeleted: Boolean? = false,
    @SerializedName("taskCount") val taskCount: String?
)
