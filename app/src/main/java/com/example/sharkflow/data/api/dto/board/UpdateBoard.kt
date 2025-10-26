package com.example.sharkflow.data.api.dto.board

import com.google.gson.annotations.SerializedName

data class UpdateBoardRequestDto(
    @SerializedName("title") val title: String? = null,
    @SerializedName("color") val color: String? = null,
    @SerializedName("isPinned") val isPinned: Boolean? = false,
    @SerializedName("isFavorite") val isFavorite: Boolean? = false
)

data class UpdateBoardResponseDto(
    @SerializedName("updatedBoard") val updatedBoard: UpdateBoardRequestDto
)
