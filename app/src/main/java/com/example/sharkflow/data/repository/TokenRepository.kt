package com.example.sharkflow.data.repository

import com.example.sharkflow.data.storage.*

class TokenRepository(private val storage: TokenStorage) {
    fun saveTokens(accessToken: String, csrfToken: String?) =
        storage.saveTokens(accessToken, csrfToken)

    fun loadTokens(): Pair<String?, String?> = storage.loadTokens()
    fun clearTokens() = storage.clearTokens()
}
