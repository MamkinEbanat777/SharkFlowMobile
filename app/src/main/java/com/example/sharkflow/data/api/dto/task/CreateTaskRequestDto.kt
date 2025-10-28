package com.example.sharkflow.data.api.dto.task

import com.example.sharkflow.domain.model.*
import com.google.gson.annotations.SerializedName

data class CreateTaskRequestDto(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("dueDate") val dueDate: String? = null,
    @SerializedName("status") val status: TaskStatus = TaskStatus.PENDING,
    @SerializedName("priority") val priority: TaskPriority = TaskPriority.MEDIUM,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null
)

