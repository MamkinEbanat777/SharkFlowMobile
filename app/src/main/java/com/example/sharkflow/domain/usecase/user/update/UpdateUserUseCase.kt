package com.example.sharkflow.domain.usecase.user.update

import com.example.sharkflow.data.repository.combined.UserRepositoryCombined
import com.example.sharkflow.domain.manager.UserManager
import jakarta.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepositoryCombined: UserRepositoryCombined,
    private val userManager: UserManager
) {
    suspend operator fun invoke(code: String, email: String, login: String): Result<String> {
        return userRepositoryCombined.confirmUpdateUser(code, email, login).map { resp ->
            userManager.setUser(resp.user)
            resp.message
        }
    }
}
