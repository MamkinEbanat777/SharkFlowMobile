package com.example.sharkflow.domain.usecase.user.update

import com.example.sharkflow.data.api.dto.user.UpdateUserAvatarResponseDto
import com.example.sharkflow.domain.repository.UserRepository
import jakarta.inject.Inject

class UpdateUserAvatarUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        avatarUrl: String,
        publicId: String
    ): Result<UpdateUserAvatarResponseDto> {
        return userRepository.updateUserAvatar(avatarUrl, publicId)
    }
}
