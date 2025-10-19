package com.example.sharkflow.domain.usecase

import com.example.sharkflow.domain.repository.*
import jakarta.inject.Inject

class UploadUserAvatarUseCase @Inject constructor(
    private val cloudinaryRepository: CloudinaryRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(imageBytes: ByteArray): Result<Pair<String, String>> {
        val (accessToken, csrfToken) = tokenRepository.loadTokens()

        val result = cloudinaryRepository.uploadImage(imageBytes, accessToken, csrfToken)

        return result.fold(
            onSuccess = { it -> Result.success(it) },
            onFailure = { e -> Result.failure(Exception("Не удалось загрузить аватар", e)) }
        )
    }
}