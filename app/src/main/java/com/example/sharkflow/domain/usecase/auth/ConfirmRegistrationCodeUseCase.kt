package com.example.sharkflow.domain.usecase.auth

import com.example.sharkflow.domain.repository.RegisterRepository
import jakarta.inject.Inject

class ConfirmRegistrationCodeUseCase @Inject constructor(
    private val registerRepository: RegisterRepository
) {
    suspend operator fun invoke(code: String): Result<String> {
        return registerRepository.confirmCode(code)
    }
}
