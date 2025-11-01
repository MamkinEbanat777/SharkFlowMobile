package com.example.sharkflow.domain.manager

import com.example.sharkflow.core.system.AppLog
import com.example.sharkflow.domain.repository.TokenRepository
import jakarta.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    private val _accessToken = MutableStateFlow<String?>(null)
    private val _csrfToken = MutableStateFlow<String?>(null)

    val hasToken: StateFlow<Boolean> = _accessToken
        .map { !it.isNullOrBlank() }
        .stateIn(
            CoroutineScope(Dispatchers.Default + SupervisorJob()),
            SharingStarted.Eagerly,
            false
        )

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        scope.launch {
            try {
                val (a, c) = tokenRepository.loadTokens()
                _accessToken.value = a
                _csrfToken.value = c
            } catch (e: Throwable) {
                AppLog.e("TokenManager", "Ошибка загрузки токенов", e)
            }
        }
    }

    fun getCurrentAccessToken(): String? = _accessToken.value
    fun getCurrentCsrfToken(): String? = _csrfToken.value

    suspend fun setTokens(accessToken: String?, csrfToken: String?) {
        if (accessToken.isNullOrBlank()) {
            clearTokens()
            return
        }
        tokenRepository.saveTokens(accessToken, csrfToken)
        _accessToken.value = accessToken
        _csrfToken.value = csrfToken
    }

    suspend fun clearTokens() {
        tokenRepository.clearTokens()
        _accessToken.value = null
        _csrfToken.value = null
    }
}
