package com.example.sharkflow.domain.usecase.user.delete

import com.example.sharkflow.data.repository.combined.UserRepositoryCombined
import com.example.sharkflow.domain.manager.UserManager
import jakarta.inject.Inject

class DeleteUserAvatarUseCase @Inject constructor(
    private val userRepositoryCombined: UserRepositoryCombined,
    private val userManager: UserManager
) {
    suspend operator fun invoke(): Result<String> {
        userManager.clearAvatar()
        return userRepositoryCombined.deleteUserAvatar()
    }
}
