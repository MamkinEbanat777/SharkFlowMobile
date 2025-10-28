package com.example.sharkflow.data.api.dto.task

import com.google.gson.annotations.SerializedName

data class DeleteTaskResponseDto(
    @SerializedName("message") val message: String,
    @SerializedName("deletedTask") val deletedTask: DeletedTaskInfoDto
)