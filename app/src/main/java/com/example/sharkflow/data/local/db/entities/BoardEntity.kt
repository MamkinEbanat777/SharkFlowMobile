package com.example.sharkflow.data.local.db.entities

import androidx.room.*

@Entity(tableName = "boards")
data class BoardEntity(
    @PrimaryKey val id: Int,
    val uuid: String,
    val title: String,
    val color: String = "FFFFFF",
    val userId: Int,
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
    val isDeleted: Boolean = false
)
