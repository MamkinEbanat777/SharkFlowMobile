package com.example.sharkflow.domain.usecase.user.update

import com.example.sharkflow.domain.manager.UserManager
import com.example.sharkflow.domain.repository.UserRepository
import jakarta.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val userManager: UserManager
) {
    suspend operator fun invoke(code: String, email: String, login: String): Result<String> {
        return userRepository.confirmUpdateUser(code, email, login).map { resp ->
            userManager.setUser(resp.user)
            resp.message
        }
    }
}
