package com.example.sharkflow.data.api.dto.board

import com.google.gson.annotations.SerializedName

data class BoardsListResponseDto(
    @SerializedName("boards") val boards: List<BoardResponseDto>,
    @SerializedName("totalBoards") val totalBoards: String
)