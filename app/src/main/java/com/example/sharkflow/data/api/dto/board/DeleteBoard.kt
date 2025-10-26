package com.example.sharkflow.data.api.dto.board

import com.google.gson.annotations.SerializedName

data class DeletedBoardInfoDto(
    @SerializedName("title") val title: String,
    @SerializedName("tasksRemoved") val tasksRemoved: String
)

data class DeleteBoardResponseDto(
    @SerializedName("message") val message: String,
    @SerializedName("deletedBoard") val deletedBoard: DeletedBoardInfoDto
)
