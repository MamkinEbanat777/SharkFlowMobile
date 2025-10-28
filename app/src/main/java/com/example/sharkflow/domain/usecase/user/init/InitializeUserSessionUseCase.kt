package com.example.sharkflow.domain.usecase.user.init

import com.example.sharkflow.core.system.AppLog
import com.example.sharkflow.domain.manager.UserManager
import com.example.sharkflow.domain.model.User
import com.example.sharkflow.domain.usecase.auth.*
import com.example.sharkflow.domain.usecase.user.get.LoadUserUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.*
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

        val localUserResult = loadUserUseCase()
        val localUser: User? = localUserResult.getOrNull()
        localUser?.let { userManager.setUser(it) }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val remoteUserResult = loadUserUseCase()
                val remoteUser: User? = remoteUserResult.getOrNull()
                remoteUser?.let { userManager.setUser(it) }
            } catch (e: Exception) {
                AppLog.e("Failed to load remote user", e)
            }
        }

        return localUser != null
    }
}
