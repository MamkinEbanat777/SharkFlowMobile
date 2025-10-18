package com.example.sharkflow.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface TokenRepository {
    val hasToken: StateFlow<Boolean>
    fun saveTokens(accessToken: String, csrfToken: String?)
    fun loadTokens(): Pair<String?, String?>
    fun clearTokens()
}
