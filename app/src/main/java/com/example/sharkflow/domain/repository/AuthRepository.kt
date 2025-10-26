package com.example.sharkflow.domain.repository

import com.example.sharkflow.data.api.dto.auth.*
import com.example.sharkflow.data.api.dto.common.GenericMessageResponseDto

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<LoginResponseDto>
    suspend fun logout(): Result<String>
    suspend fun refreshToken(): Result<RefreshResponseDto>
    suspend fun logoutFromAllDevices(): Result<GenericMessageResponseDto>
    suspend fun logoutFromDevice(deviceId: String): Result<GenericMessageResponseDto>
}
