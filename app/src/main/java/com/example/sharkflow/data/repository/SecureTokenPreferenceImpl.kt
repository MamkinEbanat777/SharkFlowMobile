package com.example.sharkflow.data.repository

import android.content.Context
import com.example.sharkflow.data.local.SecureTokenPreference
import com.example.sharkflow.data.storage.TokenStorage
import javax.inject.Inject

class SecureTokenPreferenceImpl @Inject constructor(private val context: Context) : TokenStorage {
    override fun saveTokens(accessToken: String, csrfToken: String?) =
        SecureTokenPreference.saveTokens(context, accessToken, csrfToken)

    override fun loadTokens(): Pair<String?, String?> =
        SecureTokenPreference.loadTokens(context)

    override fun clearTokens() =
        SecureTokenPreference.clearTokens(context)
}