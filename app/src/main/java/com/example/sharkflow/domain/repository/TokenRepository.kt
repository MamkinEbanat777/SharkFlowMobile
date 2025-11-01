package com.example.sharkflow.domain.repository

interface TokenRepository {
    suspend fun saveTokens(accessToken: String?, csrfToken: String?)
    suspend fun loadTokens(): Pair<String?, String?>
    suspend fun clearTokens()
}
