package com.example.sharkflow.data.network

import com.example.sharkflow.data.api.dto.auth.RefreshResponseDto
import com.example.sharkflow.domain.repository.TokenRepository
import com.google.gson.Gson
import jakarta.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class TokenAuthenticator @Inject constructor(
    private val tokenRepo: TokenRepository,
    private val baseUrl: String,
    private val refreshClient: OkHttpClient
) : Authenticator {
    private val refreshMutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request.header("X-Auth-Retry") != null) return null

        val refreshed = runBlocking { refreshMutex.withLock { performRefresh() } }
        if (!refreshed) {
            tokenRepo.clearTokens()
            return null
        }

        val (newAccess, newCsrf) = tokenRepo.loadTokens()
        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccess")
            .apply { newCsrf?.let { header("X-CSRF-TOKEN", it) } }
            .header("X-Auth-Retry", "1")
            .build()
    }


    private suspend fun performRefresh(): Boolean = withContext(Dispatchers.IO) {
        try {
            val refreshUrl = "${baseUrl.trimEnd('/')}/auth/refresh"
            val request = Request.Builder()
                .url(refreshUrl)
                .post("".toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()))
                .build()

            refreshClient.newCall(request).execute().use { resp ->
                if (!resp.isSuccessful) return@withContext false
                val refreshResponse =
                    Gson().fromJson(resp.body.string(), RefreshResponseDto::class.java)
                val newAccess = refreshResponse?.accessToken
                val newCsrf = refreshResponse?.csrfToken
                if (!newAccess.isNullOrBlank()) {
                    tokenRepo.saveTokens(newAccess, newCsrf)
                    true
                } else false
            }
        } catch (_: IOException) {
            false
        }
    }

}
