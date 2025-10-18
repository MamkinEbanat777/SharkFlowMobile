package com.example.sharkflow.domain.model

data class AuthToken(
    val accessToken: String,
    val csrfToken: String,
    val deviceId: String? = null
)
