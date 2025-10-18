package com.example.sharkflow.data.local.db.entities

import androidx.room.*

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val uuid: String,
    val login: String,
    val email: String,
    val avatarUrl: String?,
    val publicId: String?,
    val role: String
)
