package com.example.sharkflow.data.api.dto.board

import com.google.gson.annotations.SerializedName

data class UpdateBoardResponseDto(
    @SerializedName("updatedBoard") val updatedBoard: UpdateBoardRequestDto
)