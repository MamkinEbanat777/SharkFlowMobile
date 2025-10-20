package com.example.sharkflow.domain.usecase.auth

import com.example.sharkflow.domain.manager.TokenManager
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class CheckTokenUseCase @Inject constructor(
    private val tokenManager: TokenManager
) {
    operator fun invoke(): Flow<Boolean> = tokenManager.hasToken
}
