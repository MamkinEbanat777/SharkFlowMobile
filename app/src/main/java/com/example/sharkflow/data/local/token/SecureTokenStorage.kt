package com.example.sharkflow.data.local.token

import android.content.Context
import androidx.core.content.edit
import com.example.sharkflow.utils.*

object SecureTokenStorage {
    private const val PREFS_NAME = "secure_tokens_enc"
    private const val ACCESS_TOKEN_KEY = "access_token"
    private const val CSRF_TOKEN_KEY = "csrf_token"
    private fun prefs(context: Context) =
        context.applicationContext.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

    fun saveTokens(context: Context, accessToken: String, csrfToken: String?) {
        try {
            val encAccess = SecureCrypto.encryptToBase64(accessToken)
            val encCsrf = csrfToken?.let { SecureCrypto.encryptToBase64(it) }
            val p = prefs(context)
            p.edit {
                putString(ACCESS_TOKEN_KEY, encAccess)
                    .putString(CSRF_TOKEN_KEY, encCsrf)
            }
        } catch (e: Exception) {
            AppLog.e("saveTokens failed: ${e.message}", e)
        }
    }

    fun loadTokens(context: Context): Pair<String?, String?> {
        val p = prefs(context)
        val encAccess = p.getString(ACCESS_TOKEN_KEY, null)
        val encCsrf = p.getString(CSRF_TOKEN_KEY, null)
        return try {
            val access = encAccess?.let { SecureCrypto.decryptFromBase64(it) }
            val csrf = encCsrf?.let { SecureCrypto.decryptFromBase64(it) }
            access to csrf
        } catch (e: Exception) {
            AppLog.e("loadTokens/decrypt failed: ${e.message}", e)
            null to null
        }
    }

    fun clearTokens(context: Context) {
        prefs(context).edit { remove(ACCESS_TOKEN_KEY).remove(CSRF_TOKEN_KEY) }
    }
}