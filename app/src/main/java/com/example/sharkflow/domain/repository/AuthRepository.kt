package com.example.sharkflow.domain.repository

import com.example.sharkflow.data.api.AuthApi
import com.example.sharkflow.data.service.AuthService
import com.example.sharkflow.domain.model.*
import com.example.sharkflow.utils.AppLog
import jakarta.inject.*
import okhttp3.OkHttpClient

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val authService: AuthService
) {
    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val loginRequest = LoginRequest(email, password)
            val userWrapper = LoginUser(user = loginRequest)

            val loginResponse = authApi.login(userWrapper)

            if (!loginResponse.isSuccessful) {
                return Result.failure(Exception("Ошибка авторизации: ${loginResponse.code()}"))
            }

            val tokens = loginResponse.body()
            if (tokens == null || tokens.accessToken.isNullOrBlank() || tokens.csrfToken.isNullOrBlank()) {
                return Result.failure(Exception("Не удалось получить токены"))
            }

            authService.handleLogin(tokens)
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<String> {
        return try {
            val response = authApi.logout()
            if (response.isSuccessful) {
                authService.handleLogout()
                Result.success(response.body()?.message ?: "Вы успешно вышли")
            } else {
                Result.failure(Exception("Ошибка: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun refreshToken(baseClient: OkHttpClient, baseUrl: String): Boolean {
        return try {
            authService.refreshToken(baseClient, baseUrl)
        } catch (e: Exception) {
            AppLog.e("AuthRepository: refreshToken failed", e)
            false
        }
    }
}

