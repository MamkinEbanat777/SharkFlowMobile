package com.example.sharkflow.data.repository

import com.example.sharkflow.data.storage.TokenStorage
import com.example.sharkflow.domain.repository.TokenRepository
import jakarta.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepositoryImpl @Inject constructor(
    private val storage: TokenStorage
) : TokenRepository {
    override fun saveTokens(accessToken: String?, csrfToken: String?) {
        storage.saveTokens(accessToken ?: "", csrfToken)
    }

    override fun loadTokens(): Pair<String?, String?> {
        return storage.loadTokens()
    }

    override fun clearTokens() {
        storage.clearTokens()
    }
}
