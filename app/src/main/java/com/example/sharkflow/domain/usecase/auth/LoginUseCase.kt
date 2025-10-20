package com.example.sharkflow.domain.usecase.auth

import com.example.sharkflow.data.mapper.AuthMapper
import com.example.sharkflow.domain.manager.*
import com.example.sharkflow.domain.repository.*
import com.example.sharkflow.utils.flatMap
import jakarta.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val userManager: UserManager,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        return authRepository.login(email, password).flatMap { loginDto ->
            val authToken = AuthMapper.fromLoginResponse(loginDto)
            if (authToken == null) {
                Result.failure(IllegalArgumentException("Token missing or invalid"))
            } else {
                tokenManager.setTokens(authToken.accessToken, authToken.csrfToken)
                userRepository.loadUser().map { user ->
                    userManager.setUser(user)
                    "Добро пожаловать, ${user.login}"
                }
            }
        }
    }
}