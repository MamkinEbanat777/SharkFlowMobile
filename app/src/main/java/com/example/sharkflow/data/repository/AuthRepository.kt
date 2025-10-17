package com.example.sharkflow.data.repository

import com.example.sharkflow.data.api.*
import com.example.sharkflow.model.*
import com.example.sharkflow.utils.AppLog
import jakarta.inject.*

@Singleton
class AuthRepository @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository,
    private val authApi: AuthApi,
    private val userApi: UserApi
) {
    suspend fun login(email: String, password: String): Result<UserResponse> {
        return try {
            val loginRequest = LoginRequest(email, password)
            val userWrapper = LoginUser(user = loginRequest)
            val loginResponse = authApi.login(userWrapper)

            val rawBody: String? = try {
                if (loginResponse.isSuccessful) {
                    loginResponse.body()?.let { com.google.gson.Gson().toJson(it) }
                } else {
                    loginResponse.errorBody()?.string()
                }
            } catch (e: Exception) {
                AppLog.e("AuthRepository: failed to read raw body", e)
                null
            }

            val tokens = loginResponse.body()
            if (tokens == null || tokens.accessToken.isNullOrBlank() || tokens.csrfToken.isNullOrBlank()) {
                return Result.failure(Exception("Не удалось получить токены"))
            }

            try {
                tokenRepository.saveTokens(tokens.accessToken, tokens.csrfToken)
            } catch (e: Exception) {
                AppLog.e("TokenRepository.saveTokens failed", e)
            }

            val userResponse = userApi.getUser()
            if (userResponse.isSuccessful) {
                userResponse.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Не удалось загрузить пользователя"))
            } else {
                Result.failure(Exception("Ошибка получения данных о пользователе"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<String> {
        return try {
            val response = userApi.logoutUser()
            if (response.isSuccessful) {
                tokenRepository.clearTokens()
                userRepository.clearUser()
                Result.success(response.body()?.message ?: "Вы успешно вышли")
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

