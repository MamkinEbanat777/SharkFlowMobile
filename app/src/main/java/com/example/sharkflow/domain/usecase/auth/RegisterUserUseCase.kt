package com.example.sharkflow.domain.usecase.auth

import com.example.sharkflow.domain.repository.RegisterRepository
import jakarta.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val registerRepository: RegisterRepository
) {
    suspend operator fun invoke(
        login: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<Unit> {
        return registerRepository.register(login, email, password, confirmPassword)
    }
}
