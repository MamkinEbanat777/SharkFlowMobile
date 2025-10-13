package com.example.sharkflow.data.storage

interface TokenStorage {
    fun saveTokens(accessToken: String, csrfToken: String?)
    fun loadTokens(): Pair<String?, String?>
    fun clearTokens()
}
