package com.example.sharkflow.model

data class Refresh(
    val accessToken: String?,
    val csrfToken: String?,
    val message: String?,
    val error: String?,
)