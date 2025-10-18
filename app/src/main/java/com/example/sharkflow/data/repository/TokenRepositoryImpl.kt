package com.example.sharkflow.data.repository

import com.example.sharkflow.data.manager.TokenManager
import com.example.sharkflow.data.storage.TokenStorage
import com.example.sharkflow.domain.repository.TokenRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Singleton

@Singleton
class TokenRepositoryImpl @Inject constructor(
    private val storage: TokenStorage,
    private val tokenManager: TokenManager
) : TokenRepository {

    override val hasToken: StateFlow<Boolean> = tokenManager.hasToken

    override fun saveTokens(accessToken: String, csrfToken: String?) {
        storage.saveTokens(accessToken, csrfToken)
        tokenManager.setHasToken(accessToken.isNotBlank())
    }

    override fun loadTokens(): Pair<String?, String?> {
        return storage.loadTokens()
    }

    override fun clearTokens() {
        storage.clearTokens()
        tokenManager.setHasToken(false)
    }
}
