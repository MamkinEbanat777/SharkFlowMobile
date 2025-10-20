package com.example.sharkflow.domain.usecase.user.auth

import com.example.sharkflow.domain.manager.*
import com.example.sharkflow.domain.repository.AuthRepository
import jakarta.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val userManager: UserManager
) {
    suspend operator fun invoke(): Result<String> {
        tokenManager.clearTokens()
        userManager.clearUser()
        return authRepository.logout()
    }
}
