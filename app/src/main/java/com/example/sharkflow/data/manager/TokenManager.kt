package com.example.sharkflow.data.manager

import com.example.sharkflow.data.storage.TokenStorage
import jakarta.inject.*
import kotlinx.coroutines.flow.*

@Singleton
class TokenManager @Inject constructor(
    private val storage: TokenStorage
) {
    private val _hasToken = MutableStateFlow(storage.loadTokens().first != null)
    val hasToken: StateFlow<Boolean> = _hasToken.asStateFlow()

    fun updateTokenState() {
        _hasToken.value = storage.loadTokens().first != null
    }

    fun setHasToken(hasToken: Boolean) {
        _hasToken.value = hasToken
    }
}
