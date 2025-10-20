package com.example.sharkflow.domain.usecase.user.delete

import com.example.sharkflow.domain.repository.UserRepository
import jakarta.inject.Inject

class RequestDeleteUserCodeUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<String> {
        return userRepository.requestDeleteUserCode()
    }
}
