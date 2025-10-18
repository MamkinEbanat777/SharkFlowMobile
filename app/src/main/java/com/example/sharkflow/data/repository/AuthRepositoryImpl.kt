package com.example.sharkflow.data.repository

import com.example.sharkflow.data.api.AuthApi
import com.example.sharkflow.data.api.dto.auth.LoginResponseDto
import com.example.sharkflow.data.manager.AuthManager
import com.example.sharkflow.data.mapper.AuthMapper
import com.example.sharkflow.domain.model.*
import com.example.sharkflow.domain.repository.AuthRepository
import jakarta.inject.*
import retrofit2.Response

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val authManager: AuthManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthToken> {
        return try {
            val params = LoginParams(email = email, password = password)
            val requestDto = AuthMapper.toLoginUserDto(params)

            val response: Response<LoginResponseDto> =
                authApi.login(requestDto)

            if (!response.isSuccessful) {
                return Result.failure(Exception("Auth error: ${response.code()}"))
            }

            val body = response.body()
            val token = body?.let { AuthMapper.fromLoginResponse(it) }
                ?: return Result.failure(Exception("No tokens in response"))

            authManager.handleLogin(token)

            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<String> {
        return try {
            val resp = authApi.logout()
            if (resp.isSuccessful) {
                authManager.handleLogout()
                Result.success(resp.body()?.message ?: "Logged out")
            } else {
                Result.failure(Exception("Logout error: ${resp.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshToken(): Boolean {
        return authManager.refreshToken()
    }
}
