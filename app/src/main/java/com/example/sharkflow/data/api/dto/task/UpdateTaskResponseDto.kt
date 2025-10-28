package com.example.sharkflow.data.api.dto.task

import com.google.gson.annotations.SerializedName

data class UpdateTaskResponseDto(
    @SerializedName("message") val message: String,
    @SerializedName("updated") val updated: UpdateTaskRequestDto
)
