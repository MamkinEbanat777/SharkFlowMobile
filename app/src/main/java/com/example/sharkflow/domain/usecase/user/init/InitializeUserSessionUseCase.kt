package com.example.sharkflow.domain.usecase.user.init

import com.example.sharkflow.domain.manager.UserManager
import com.example.sharkflow.domain.usecase.auth.*
import com.example.sharkflow.domain.usecase.user.get.LoadUserUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first

class InitializeUserSessionUseCase @Inject constructor(
    private val checkTokenUseCase: CheckTokenUseCase,
    private val clearTokensUseCase: ClearTokensUseCase,
    private val loadUserUseCase: LoadUserUseCase,
    private val userManager: UserManager
) {
    suspend operator fun invoke(): Boolean {
        val hasToken = checkTokenUseCase().first()
        if (hasToken) {
            val user = loadUserUseCase().getOrNull() ?: return false
            userManager.setUser(user)
            return true
        } else {
            clearTokensUseCase()
            return false
        }
    }
}