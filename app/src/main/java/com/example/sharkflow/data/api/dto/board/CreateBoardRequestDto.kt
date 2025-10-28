package com.example.sharkflow.data.api.dto.board

import com.google.gson.annotations.SerializedName

data class CreateBoardRequestDto(
    @SerializedName("title") val title: String,
    @SerializedName("color") val color: String
)

