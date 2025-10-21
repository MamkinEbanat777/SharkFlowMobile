package com.example.sharkflow.domain.usecase.user.update

import com.example.sharkflow.data.api.dto.user.UpdateUserAvatarResponseDto
import com.example.sharkflow.data.repository.combined.UserRepositoryCombined
import jakarta.inject.Inject

class UpdateUserAvatarUseCase @Inject constructor(
    private val userRepositoryCombined: UserRepositoryCombined,
) {
    suspend operator fun invoke(
        avatarUrl: String,
        publicId: String
    ): Result<UpdateUserAvatarResponseDto> {
        return userRepositoryCombined.updateUserAvatar(avatarUrl, publicId)
    }
}
