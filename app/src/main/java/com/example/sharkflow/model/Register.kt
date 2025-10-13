package com.example.sharkflow.model

data class RegisterRequest(
    val login: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val acceptedPolicies: Boolean = true,
)

data class RegisterUser(
    val user: RegisterRequest
)

data class RegisterResponse(
    val message: String?,
    val error: String?
)