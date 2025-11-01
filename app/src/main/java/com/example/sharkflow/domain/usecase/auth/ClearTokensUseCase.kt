package com.example.sharkflow.domain.usecase.auth

import com.example.sharkflow.domain.manager.TokenManager
import jakarta.inject.Inject

class ClearTokensUseCase @Inject constructor(
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke() {
        tokenManager.clearTokens()
    }
}