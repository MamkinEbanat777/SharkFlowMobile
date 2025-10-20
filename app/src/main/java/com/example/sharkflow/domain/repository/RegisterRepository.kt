package com.example.sharkflow.domain.repository

interface RegisterRepository {
    suspend fun register(
        login: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<Unit>

    suspend fun confirmCode(code: String): Result<String>
}
