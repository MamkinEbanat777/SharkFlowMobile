package com.example.sharkflow.domain.usecase

import com.example.sharkflow.data.manager.AuthManager
import com.example.sharkflow.data.mapper.AuthMapper
import com.example.sharkflow.domain.repository.*
import jakarta.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val authManager: AuthManager
) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        val loginResponse = authRepository.login(email, password)
        if (loginResponse.isFailure) {
            return Result.failure(loginResponse.exceptionOrNull()!!)
        }

        val loginDto = loginResponse.getOrNull()!!
        val authToken = AuthMapper.fromLoginResponse(loginDto)

        if (authToken == null) {
            return Result.failure(IllegalArgumentException("Token is missing or invalid"))
        }
        
        authManager.handleLogin(authToken)

        val userResult = userRepository.loadUser()
        if (userResult.isFailure) {
            return Result.failure(userResult.exceptionOrNull()!!)
        }

        val user = userResult.getOrNull()!!
        return Result.success("Добро пожаловать, ${user.login}")
    }
}
