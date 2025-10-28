package com.example.sharkflow.data.api.dto.task

import com.google.gson.annotations.SerializedName

data class TasksListResponseDto(
    @SerializedName("tasks") val tasks: List<TaskResponseDto>
)