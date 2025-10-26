package com.example.sharkflow.domain.usecase.user.delete

import com.example.sharkflow.domain.repository.UserRepositoryCombined
import jakarta.inject.Inject

class RequestDeleteUserCodeUseCase @Inject constructor(
    private val userRepositoryCombined: UserRepositoryCombined
) {
    suspend operator fun invoke(): Result<String> {
        return userRepositoryCombined.requestDeleteUserCode()
    }
}
