package com.example.sharkflow.data.manager

import com.example.sharkflow.data.api.dto.auth.RefreshResponseDto
import com.example.sharkflow.data.mapper.AuthMapper
import com.example.sharkflow.domain.model.AuthToken
import com.example.sharkflow.domain.repository.TokenRepository
import com.example.sharkflow.utils.AppLog
import com.google.gson.Gson
import jakarta.inject.*
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@Singleton
class AuthManager @Inject constructor(
    private val tokenRepo: TokenRepository,
    private val baseClient: OkHttpClient,
    private val baseUrl: String
) {
    private val gson = Gson()

    suspend fun refreshToken(): Boolean {
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
                    val dto = try {
                        gson.fromJson(body, RefreshResponseDto::class.java)
                    } catch (e: Exception) {
                        AppLog.e("AuthManager", "refresh parse failed", e)
                        null
                    } ?: return@withContext false

                    val token = AuthMapper.fromRefreshResponse(dto)
                        ?: return@withContext false

                    try {
                        tokenRepo.saveTokens(token.accessToken, token.csrfToken)
                    } catch (e: Exception) {
                        AppLog.e("AuthManager", "Failed to save tokens", e)
                        return@withContext false
                    }

                    return@withContext true
                }
            } catch (_: Exception) {
                false
            }
        }
    }

    fun handleLogin(token: AuthToken) {
        val access = token.accessToken
        val csrf = token.csrfToken

        if (access.isNotBlank() && csrf.isNotBlank()) {
            tokenRepo.saveTokens(access, csrf)
        } else {
            throw IllegalArgumentException("Invalid tokens on login")
        }
    }

    fun handleLogout() {
        tokenRepo.clearTokens()
    }

}