package com.example.sharkflow.data.local.db.entities

import androidx.room.*

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: Int,
    val uuid: String,
    val title: String,
    val description: String?,
    val boardId: Int,
    val status: String = "PENDING",
    val priority: String = "MEDIUM",
    val isDeleted: Boolean = false
)
