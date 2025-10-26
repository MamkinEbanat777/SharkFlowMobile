package com.example.sharkflow.domain.usecase.user.update

import com.example.sharkflow.domain.repository.UserRepositoryCombined
import jakarta.inject.Inject

class RequestUpdateUserCodeUseCase @Inject constructor(
    private val userRepositoryCombined: UserRepositoryCombined,
) {
    suspend operator fun invoke(): Result<String> {
        return userRepositoryCombined.requestUpdateUserCode()
    }
}
