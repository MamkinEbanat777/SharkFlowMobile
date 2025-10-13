package com.example.sharkflow.model

data class UserResponse(
    val login: String?,
    val email: String?,
    val role: String?,
    val avatarUrl: String?
)

data class LogoutResponse(
    val message: String?,
    val error: String?
)