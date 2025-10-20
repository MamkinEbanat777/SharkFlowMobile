package com.example.sharkflow.domain.usecase.user.delete

import com.example.sharkflow.domain.manager.UserManager
import com.example.sharkflow.domain.repository.UserRepository
import jakarta.inject.Inject

class DeleteUserAvatarUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val userManager: UserManager
) {
    suspend operator fun invoke(): Result<String> {
        userManager.clearAvatar()
        return userRepository.deleteUserAvatar()
    }
}
