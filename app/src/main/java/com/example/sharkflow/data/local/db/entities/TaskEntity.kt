package com.example.sharkflow.data.local.db.entities

import androidx.room.*

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val uuid: String,
    val serverUuid: String? = null,
    val title: String,
    val description: String?,
    val boardUuid: String,
    val status: String = "PENDING",
    val priority: String = "MEDIUM",
    val isDeleted: Boolean = false,
    val isSynced: Boolean = false,
    val dueDate: String?,
    val createdAt: String?,
    val updatedAt: String?
)
