package com.example.sharkflow.domain.model

import java.time.Instant

data class Board(
    val uuid: String,
    val serverUuid: String? = null,
    val title: String,
    val color: String?,
    val userUuid: String,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
    val isDeleted: Boolean = false,
    val isSynced: Boolean = false,
    val taskCount: String?,
)
