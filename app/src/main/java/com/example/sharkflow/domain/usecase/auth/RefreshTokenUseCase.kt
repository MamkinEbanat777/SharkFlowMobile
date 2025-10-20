package com.example.sharkflow.domain.usecase.auth

import com.example.sharkflow.domain.repository.*
import jakarta.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(): Boolean {
        return authRepository.refreshToken()
            .map { token ->
                tokenRepository.saveTokens(token.accessToken, token.csrfToken)
                true
            }.getOrElse { false }
    }
}
