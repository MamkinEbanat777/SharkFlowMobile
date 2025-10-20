package com.example.sharkflow.domain.usecase.user.delete

import com.example.sharkflow.domain.manager.TokenManager
import com.example.sharkflow.domain.repository.UserRepository
import jakarta.inject.Inject

class ConfirmDeleteUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(code: String): Result<String> {
        return userRepository.confirmDeleteUser(code).mapCatching { msg ->
            tokenManager.clearTokens()
            msg
        }
    }
}
