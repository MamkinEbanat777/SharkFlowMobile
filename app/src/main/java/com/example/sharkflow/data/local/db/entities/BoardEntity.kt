package com.example.sharkflow.data.local.db.entities

import androidx.room.*
import java.time.Instant

@Entity(tableName = "boards")
data class BoardEntity(
    @PrimaryKey val uuid: String,
    val title: String,
    val color: String? = "FFFFFF",
    val userUuid: String,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
    val isDeleted: Boolean = false,
    val isSynced: Boolean = false,
    val taskCount: String?,
)