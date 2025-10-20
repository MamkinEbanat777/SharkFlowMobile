package com.example.sharkflow.domain.model

data class User(
    val login: String,
    val email: String,
    val role: String,
    val avatarUrl: String?,
    val publicId: String?
)
