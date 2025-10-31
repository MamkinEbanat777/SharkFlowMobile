package com.example.sharkflow.domain.usecase.auth

import com.example.sharkflow.data.repository.local.UserLocalRepositoryImpl
import com.example.sharkflow.domain.manager.*
import com.example.sharkflow.domain.repository.AuthRepository
import jakarta.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val userManager: UserManager,
    private val local: UserLocalRepositoryImpl
) {
    suspend operator fun invoke(): Result<String> {
        val result = authRepository.logout()
        if (result.isSuccess) {
            tokenManager.clearTokens()
            userManager.clearUser()
            local.clearActiveUser()
        }
        return result

    }
}
