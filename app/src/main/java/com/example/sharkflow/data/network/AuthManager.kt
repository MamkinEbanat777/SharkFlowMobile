package com.example.sharkflow.data.network

import android.content.Context
import com.example.sharkflow.data.local.token.SecureTokenStorage
import com.example.sharkflow.model.Refresh
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

object AuthManager {
    private val gson = Gson()

    suspend fun refreshToken(context: Context, baseClient: OkHttpClient, baseUrl: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val refreshUrl = "${baseUrl}auth/refresh"
                val emptyBody =
                    "".toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                val request = Request.Builder()
                    .url(refreshUrl)
                    .post(emptyBody)
                    .build()

                baseClient.newCall(request).execute().use { resp ->
                    if (!resp.isSuccessful) return@withContext false

                    val body = resp.body.string()
                    val data = try {
                        gson.fromJson(body, Refresh::class.java)
                    } catch (_: Exception) {
                        null
                    }

                    val newAccess = data?.accessToken
                    val newCsrf = data?.csrfToken
                    if (!newAccess.isNullOrBlank()) {
                        SecureTokenStorage.saveTokens(
                            context.applicationContext,
                            newAccess,
                            newCsrf
                        )
                        return@withContext true
                    }

                    false
                }
            } catch (_: Exception) {
                false
            }
        }
    }
}

