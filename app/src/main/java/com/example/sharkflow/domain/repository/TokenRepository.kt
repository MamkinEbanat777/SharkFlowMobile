package com.example.sharkflow.domain.repository

interface TokenRepository {
    fun saveTokens(accessToken: String?, csrfToken: String?)
    fun loadTokens(): Pair<String?, String?>
    fun clearTokens()
}
