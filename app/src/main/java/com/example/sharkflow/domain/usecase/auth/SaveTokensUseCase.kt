package com.example.sharkflow.domain.usecase.auth

import com.example.sharkflow.domain.manager.TokenManager
import jakarta.inject.Inject

class SaveTokensUseCase @Inject constructor(
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(accessToken: String?, csrfToken: String?) {
        tokenManager.setTokens(accessToken, csrfToken)
    }
}