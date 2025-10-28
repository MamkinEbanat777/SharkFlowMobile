package com.example.sharkflow.data.api.dto.task

import com.example.sharkflow.domain.model.*
import com.google.gson.annotations.SerializedName

data class UpdateTaskRequestDto(
    @SerializedName("title") val title: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("dueDate") val dueDate: String? = null,
    @SerializedName("status") val status: TaskStatus? = null,
    @SerializedName("priority") val priority: TaskPriority? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null
)