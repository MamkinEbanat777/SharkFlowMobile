package com.example.sharkflow.data.local

import android.content.*
import com.example.sharkflow.data.storage.*
import javax.inject.*

class SecureTokenStorageImpl @Inject constructor(private val context: Context) : TokenStorage {
    override fun saveTokens(accessToken: String, csrfToken: String?) =
        SecureTokenStorage.saveTokens(context, accessToken, csrfToken)

    override fun loadTokens(): Pair<String?, String?> =
        SecureTokenStorage.loadTokens(context)

    override fun clearTokens() =
        SecureTokenStorage.clearTokens(context)
}

