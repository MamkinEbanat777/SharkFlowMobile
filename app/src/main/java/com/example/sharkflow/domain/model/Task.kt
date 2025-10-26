package com.example.sharkflow.domain.model

import com.example.sharkflow.data.api.dto.task.*

data class Task(
    val uuid: String,
    val title: String,
    val description: String?,
    val boardUuid: String,
    val dueDate: String?,
    val status: Status,
    val priority: Priority,
    val isDeleted: Boolean = false,
    val isSynced: Boolean = false
)
