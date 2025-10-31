package com.example.sharkflow.domain.usecase.user.update

import com.example.sharkflow.core.system.AppLog
import com.example.sharkflow.domain.manager.UserManager
import com.example.sharkflow.domain.repository.UserRepositoryCombined
import jakarta.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepositoryCombined: UserRepositoryCombined,
    private val userManager: UserManager
) {
    suspend operator fun invoke(code: String, email: String, login: String): Result<String> {
        return userRepositoryCombined.confirmUpdateUser(code, email, login).map { resp ->
            resp.user?.let { user ->
                userManager.setUser(user)
            } ?: AppLog.w(
                "UpdateUserUseCase",
                "confirmUpdateUser: response.user == null; keeping existing user"
            )
            resp.message
        }

    }
}
