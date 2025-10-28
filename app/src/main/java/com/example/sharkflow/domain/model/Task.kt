package com.example.sharkflow.domain.model

data class Task(
    val uuid: String,
    val serverUuid: String? = null,
    val title: String,
    val description: String?,
    val boardUuid: String,
    val dueDate: String?,
    val status: TaskStatus,
    val priority: TaskPriority,
    val isDeleted: Boolean = false,
    val isSynced: Boolean = false,
    val createdAt: String?,
    val updatedAt: String?
)
