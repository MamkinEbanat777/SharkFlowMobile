package com.example.sharkflow.domain.usecase.user.init

import com.example.sharkflow.domain.manager.UserManager
import com.example.sharkflow.domain.model.User
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
        if (!hasToken) {
            clearTokensUseCase()
            return false
        }

        if (userManager.currentUser.value != null) return true

        val localUserResult = loadUserUseCase()
        val localUser: User? = localUserResult.getOrNull()
        localUser?.let { userManager.setUser(it) }
        return localUser != null
    }
}
