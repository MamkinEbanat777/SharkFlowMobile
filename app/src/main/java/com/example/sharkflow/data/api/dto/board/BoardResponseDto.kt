package com.example.sharkflow.data.api.dto.board

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class BoardResponseDto(
    @SerializedName("uuid") val uuid: String,
    @SerializedName("title") val title: String,
    @SerializedName("color") val color: String,
    @SerializedName("isPinned") val isPinned: Boolean,
    @SerializedName("isFavorite") val isFavorite: Boolean,
    @SerializedName("taskCount") val taskCount: String,
    @SerializedName("createdAt") val createdAt: Instant?,
    @SerializedName("updatedAt") val updatedAt: Instant?
)

data class BoardsListResponseDto(
    @SerializedName("boards") val boards: List<BoardResponseDto>,
    @SerializedName("totalBoards") val totalBoards: String
)
