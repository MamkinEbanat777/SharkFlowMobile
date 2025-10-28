package com.example.sharkflow.data.api.dto.task

import com.google.gson.annotations.SerializedName

data class CreateTaskResponseDto(
    @SerializedName("message") val message: String,
    @SerializedName("task") val task: TaskResponseDto,
    @SerializedName("taskCount") val taskCount: Int
)
