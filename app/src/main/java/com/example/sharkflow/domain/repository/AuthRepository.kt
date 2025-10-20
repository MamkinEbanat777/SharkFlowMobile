package com.example.sharkflow.domain.repository

import com.example.sharkflow.data.api.dto.auth.*

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<LoginResponseDto>
    suspend fun logout(): Result<String>
    suspend fun refreshToken(): Result<RefreshResponseDto>
}
