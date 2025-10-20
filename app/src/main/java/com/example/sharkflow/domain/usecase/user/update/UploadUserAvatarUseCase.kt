package com.example.sharkflow.domain.usecase.user.update

import com.example.sharkflow.domain.repository.*
import jakarta.inject.Inject

class UploadUserAvatarUseCase @Inject constructor(
    private val cloudinaryRepository: CloudinaryRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(imageBytes: ByteArray): Result<Pair<String, String>> {
        return try {
            val (accessToken, csrfToken) = tokenRepository.loadTokens()
            cloudinaryRepository.uploadImage(imageBytes, accessToken, csrfToken)
        } catch (e: Exception) {
            Result.failure(Exception("Не удалось загрузить аватар: ${e.message}", e))
        }
    }
}
