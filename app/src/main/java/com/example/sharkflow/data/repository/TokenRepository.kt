package com.example.sharkflow.data.repository

import com.example.sharkflow.data.storage.TokenStorage
import kotlinx.coroutines.flow.*

class TokenRepository(private val storage: TokenStorage) {

    private val _hasToken = MutableStateFlow(storage.loadTokens().first != null)
    val hasToken: StateFlow<Boolean> = _hasToken.asStateFlow()

    fun saveTokens(accessToken: String, csrfToken: String?) {
        storage.saveTokens(accessToken, csrfToken)
        _hasToken.value = accessToken.isNotBlank()
    }

    fun loadTokens(): Pair<String?, String?> {
        val tokens = storage.loadTokens()
        _hasToken.value = !tokens.first.isNullOrBlank()
        return tokens
    }

    fun clearTokens() {
        storage.clearTokens()
        _hasToken.value = false
    }
}
