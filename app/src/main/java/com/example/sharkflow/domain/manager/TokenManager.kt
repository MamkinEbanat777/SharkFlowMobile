package com.example.sharkflow.domain.manager

import com.example.sharkflow.domain.repository.TokenRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    private val _hasToken = MutableStateFlow(false)
    val hasToken: StateFlow<Boolean> = _hasToken.asStateFlow()

    init {
        val (access, _) = tokenRepository.loadTokens()
        _hasToken.value = !access.isNullOrBlank()
    }

    fun setTokens(accessToken: String?, csrfToken: String?) {
        if (accessToken.isNullOrBlank()) {
            clearTokens()
            return
        }
        tokenRepository.saveTokens(accessToken, csrfToken)
        _hasToken.value = true
    }

    fun clearTokens() {
        tokenRepository.clearTokens()
        _hasToken.value = false
    }

    fun refreshState() {
        val (access, _) = tokenRepository.loadTokens()
        _hasToken.value = !access.isNullOrBlank()
    }
}