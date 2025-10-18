package com.example.sharkflow.data.service

import com.example.sharkflow.domain.model.*
import com.example.sharkflow.domain.repository.TokenRepository
import com.example.sharkflow.utils.AppLog
import com.google.gson.Gson
import jakarta.inject.*
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@Singleton
class AuthService @Inject constructor(
    private val tokenRepo: TokenRepository
) {
    private val gson = Gson()

    suspend fun refreshToken(
        baseClient: OkHttpClient,
        baseUrl: String
    ): Boolean {
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
                    if (!newAccess.isNullOrBlank() && newAccess != "undefined") {
                        try {
                            tokenRepo.saveTokens(newAccess, newCsrf)
                        } catch (e: Exception) {
                            AppLog.e("AuthService", "Failed to save tokens", e)
                            return@withContext false
                        }
                        return@withContext true
                    }

                    false
                }
            } catch (_: Exception) {
                false
            }
        }
    }

    fun handleLogin(tokens: LoginResponse) {
        val access = tokens.accessToken
        val csrf = tokens.csrfToken

        if (!access.isNullOrBlank() && !csrf.isNullOrBlank()) {
            tokenRepo.saveTokens(access, csrf)
        } else {
            throw IllegalArgumentException("Некорректные токены при логине")
        }
    }

    fun handleLogout() {
        tokenRepo.clearTokens()
    }

}