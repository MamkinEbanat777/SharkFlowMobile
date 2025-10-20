package com.example.sharkflow.domain.usecase.user.update

import com.example.sharkflow.domain.repository.UserRepository
import jakarta.inject.Inject

class RequestUpdateUserCodeUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<String> {
        return userRepository.requestUpdateUserCode()
    }
}
