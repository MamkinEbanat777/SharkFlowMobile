package com.example.sharkflow.domain.model

data class LoginParams(
    val email: String,
    val password: String,
    val rememberMe: Boolean = true
)
