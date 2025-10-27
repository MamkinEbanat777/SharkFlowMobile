package com.example.sharkflow.domain.usecase.user.delete

import com.example.sharkflow.domain.manager.UserManager
import com.example.sharkflow.domain.repository.UserRepositoryCombined
import jakarta.inject.Inject

class DeleteUserAvatarUseCase @Inject constructor(
    private val userRepositoryCombined: UserRepositoryCombined,
    private val userManager: UserManager
) {
    suspend operator fun invoke(): Result<String> {
        val result = userRepositoryCombined.deleteUserAvatar()
        result.onSuccess {
            userManager.clearAvatar()
        }
        return result
    }
}
