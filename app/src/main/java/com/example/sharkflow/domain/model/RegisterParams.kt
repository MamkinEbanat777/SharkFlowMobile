package com.example.sharkflow.domain.model

data class RegisterParams(
    val login: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val acceptedPolicies: Boolean = true
)
