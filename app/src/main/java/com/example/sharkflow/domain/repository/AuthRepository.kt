package com.example.sharkflow.domain.repository

import com.example.sharkflow.domain.model.AuthToken

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthToken>
    suspend fun logout(): Result<String>
    suspend fun refreshToken(): Boolean
}
