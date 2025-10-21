package com.example.sharkflow.domain.usecase.auth

import com.example.sharkflow.data.mapper.AuthMapper
import com.example.sharkflow.domain.manager.*
import com.example.sharkflow.domain.repository.AuthRepository
import com.example.sharkflow.domain.usecase.user.get.LoadUserUseCase
import com.example.sharkflow.utils.flatMap
import jakarta.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userManager: UserManager,
    private val tokenManager: TokenManager,
    private val loadUserUseCase: LoadUserUseCase
) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        return authRepository.login(email, password).flatMap { loginDto ->
            val authToken = AuthMapper.fromLoginResponse(loginDto)
            if (authToken == null) {
                Result.failure(IllegalArgumentException("Token missing or invalid"))
            } else {
                tokenManager.setTokens(authToken.accessToken, authToken.csrfToken)
                loadUserUseCase().map { user ->
                    userManager.setUser(user)
                    "Добро пожаловать, ${user.login}"
                }
            }
        }
    }
}