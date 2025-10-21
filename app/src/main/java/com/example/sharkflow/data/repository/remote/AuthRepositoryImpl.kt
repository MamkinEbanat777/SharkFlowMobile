package com.example.sharkflow.data.repository.remote

import com.example.sharkflow.data.api.AuthApi
import com.example.sharkflow.data.api.dto.auth.*
import com.example.sharkflow.data.mapper.AuthMapper
import com.example.sharkflow.domain.model.LoginParams
import com.example.sharkflow.domain.repository.AuthRepository
import com.example.sharkflow.utils.safeApiCall
import jakarta.inject.*

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<LoginResponseDto> {
        return try {
            val requestDto = AuthMapper.toLoginUserDto(LoginParams(email, password))
            safeApiCall { authApi.login(requestDto) }
        } catch (e: Exception) {
            Result.failure(Exception("Сервер недоступен, попробуйте позже"))
        }
    }

    override suspend fun logout(): Result<String> {
        return try {
            safeApiCall { authApi.logout() }.map { it.message ?: "Logged out" }
        } catch (e: Exception) {
            Result.failure(Exception("Сервер недоступен, попробуйте позже"))
        }
    }

    override suspend fun refreshToken(): Result<RefreshResponseDto> {
        return try {
            // Заглушка
            val dummyToken = RefreshResponseDto(
                accessToken = "dummyAccessToken",
                csrfToken = "dummyCsrfToken",
                message = "Token refreshed (stub)",
                error = null
            )
            Result.success(dummyToken)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}
