package com.example.sharkflow.data.repository

import com.example.sharkflow.data.api.*
import com.example.sharkflow.model.*
import com.example.sharkflow.utils.ErrorMapper
import jakarta.inject.*

@Singleton
class AuthRepository @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val authApi: AuthApi,
    private val userApi: UserApi
) {
    suspend fun login(email: String, password: String): Result<UserResponse> {
        return try {
            val loginRequest = LoginRequest(email, password)
            val userWrapper = LoginUser(user = loginRequest)

            val loginResponse = authApi.login(userWrapper)
            if (!loginResponse.isSuccessful) {
                return Result.failure(
                    Exception(
                        ErrorMapper.map(
                            loginResponse.code(),
                            loginResponse.errorBody()?.string()
                        )
                    )
                )
            }

            val tokens = loginResponse.body()
            if (tokens == null || tokens.accessToken.isNullOrBlank() || tokens.csrfToken.isNullOrBlank()) {
                return Result.failure(Exception("Не удалось получить токены"))
            }

            tokenRepository.saveTokens(tokens.accessToken, tokens.csrfToken)

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
}

