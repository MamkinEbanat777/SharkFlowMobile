package com.example.sharkflow.data.repository.remote

import com.example.sharkflow.data.storage.TokenStorage
import com.example.sharkflow.domain.repository.TokenRepository
import jakarta.inject.Inject
import kotlinx.coroutines.*
import javax.inject.Singleton

@Singleton
class TokenRepositoryImpl @Inject constructor(
    private val storage: TokenStorage
) : TokenRepository {
    override suspend fun saveTokens(accessToken: String?, csrfToken: String?) {
        withContext(Dispatchers.IO) {
            storage.saveTokens(accessToken ?: "", csrfToken)
        }
    }

    override suspend fun loadTokens(): Pair<String?, String?> {
        return withContext(Dispatchers.IO) {
            storage.loadTokens()
        }
    }

    override suspend fun clearTokens() {
        withContext(Dispatchers.IO) {
            storage.clearTokens()
        }
    }
}