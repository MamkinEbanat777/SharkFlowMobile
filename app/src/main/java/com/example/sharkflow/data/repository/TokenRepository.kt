package com.example.sharkflow.data.repository

import com.example.sharkflow.data.manager.TokenManager
import com.example.sharkflow.data.storage.TokenStorage
import kotlinx.coroutines.flow.StateFlow

class TokenRepository(
    private val storage: TokenStorage,
    private val tokenManager: TokenManager
) {
    val hasToken: StateFlow<Boolean> = tokenManager.hasToken

    fun saveTokens(accessToken: String, csrfToken: String?) {
        storage.saveTokens(accessToken, csrfToken)
        tokenManager.setHasToken(accessToken.isNotBlank())
    }

    fun loadTokens(): Pair<String?, String?> {
        return storage.loadTokens()
    }

    fun clearTokens() {
        storage.clearTokens()
        tokenManager.setHasToken(false)
    }
}
