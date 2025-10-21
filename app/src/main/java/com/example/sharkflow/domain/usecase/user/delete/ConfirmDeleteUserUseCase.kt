package com.example.sharkflow.domain.usecase.user.delete

import com.example.sharkflow.data.repository.combined.UserRepositoryCombined
import com.example.sharkflow.domain.manager.*
import jakarta.inject.Inject

class ConfirmDeleteUserUseCase @Inject constructor(
    private val userRepositoryCombined: UserRepositoryCombined,
    private val tokenManager: TokenManager,
    private val userManager: UserManager
) {
    suspend operator fun invoke(code: String): Result<String> {
        return userRepositoryCombined.confirmDeleteUser(code).mapCatching { msg ->
            userManager.clearUser()
            tokenManager.clearTokens()
            msg
        }
    }
}
