package com.example.sharkflow.domain.usecase.user.delete

import com.example.sharkflow.domain.usecase.auth.ClearTokensUseCase
import jakarta.inject.Inject

class DeleteUserAccountUseCase @Inject constructor(
    private val confirmDeleteUserUseCase: ConfirmDeleteUserUseCase,
    private val clearTokensUseCase: ClearTokensUseCase
) {
    suspend operator fun invoke(code: String): Result<String> {
        return confirmDeleteUserUseCase(code).mapCatching { message ->
            clearTokensUseCase()
            message
        }
    }
}
