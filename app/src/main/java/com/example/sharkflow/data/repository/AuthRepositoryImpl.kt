package com.example.sharkflow.data.repository

import com.example.sharkflow.data.api.AuthApi
import com.example.sharkflow.data.api.dto.auth.LoginResponseDto
import com.example.sharkflow.data.mapper.AuthMapper
import com.example.sharkflow.domain.model.LoginParams
import com.example.sharkflow.domain.repository.AuthRepository
import jakarta.inject.*

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<LoginResponseDto> {
        return try {
            val requestDto = AuthMapper.toLoginUserDto(LoginParams(email, password))
            val response = authApi.login(requestDto)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Logout error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Logout request failed: ${e.message}", e))
        }
    }

    override suspend fun logout(): Result<String> {
        return try {
            val resp = authApi.logout()
            if (resp.isSuccessful) {
                Result.success(resp.body()?.message ?: "Logged out")
            } else {
                Result.failure(Exception("Logout error: ${resp.code()} - ${resp.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Logout request failed: ${e.message}", e))
        }
    }

    override suspend fun refreshToken(): Boolean {
        return true
    }
}
