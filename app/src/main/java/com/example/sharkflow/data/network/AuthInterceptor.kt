package com.example.sharkflow.data.network

import com.example.sharkflow.data.api.dto.auth.RefreshResponseDto
import com.example.sharkflow.domain.repository.TokenRepository
import com.google.gson.Gson
import jakarta.inject.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenRepo: TokenRepository,
    private val baseUrl: String
) : Interceptor {

    private val lock = ReentrantLock()
    private val refreshComplete = lock.newCondition()

    @Volatile
    private var isRefreshing = false

    companion object {
        private const val RETRY_HEADER = "X-Auth-Retry"
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val X_CSRF_HEADER = "X-CSRF-TOKEN"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val (accessToken, csrfToken) = tokenRepo.loadTokens()

        if (!accessToken.isNullOrBlank()) {
            request = request.newBuilder()
                .header(AUTHORIZATION_HEADER, "Bearer $accessToken")
                .apply {
                    csrfToken?.let { header(X_CSRF_HEADER, it) }
                }
                .build()
        }

        val response = chain.proceed(request)

        if ((response.code == 401 || response.code == 403) && request.header(RETRY_HEADER) == null) {
            response.close()

            lock.withLock {
                if (!isRefreshing) {
                    isRefreshing = true
                    try {
                        val refreshed = performRefresh()
                        refreshComplete.signalAll()
                        isRefreshing = false

                        if (refreshed) {
                            val (newAccess, newCsrf) = tokenRepo.loadTokens()

                            if (!newAccess.isNullOrBlank()) {
                                val retryReq = request.newBuilder()
                                    .header(AUTHORIZATION_HEADER, "Bearer $newAccess")
                                    .apply { newCsrf?.let { header(X_CSRF_HEADER, it) } }
                                    .header(RETRY_HEADER, "1")
                                    .build()
                                return chain.proceed(retryReq)
                            }
                        } else {
                            tokenRepo.clearTokens()
                        }
                    } finally {
                        if (isRefreshing) {
                            isRefreshing = false
                            refreshComplete.signalAll()
                        }
                    }
                } else {
                    try {
                        while (isRefreshing) {
                            refreshComplete.await(5, TimeUnit.SECONDS)
                        }
                    } catch (_: InterruptedException) {
                        Thread.currentThread().interrupt()
                    }

                    val (newAccess, newCsrf) = tokenRepo.loadTokens()

                    if (!newAccess.isNullOrBlank()) {
                        val retryReq = request.newBuilder()
                            .header(AUTHORIZATION_HEADER, "Bearer $newAccess")
                            .apply { newCsrf?.let { header(X_CSRF_HEADER, it) } }
                            .header(RETRY_HEADER, "1")
                            .build()
                        return chain.proceed(retryReq)
                    }
                }
            }
        }

        return response
    }

    private fun performRefresh(): Boolean {
        return try {
            val refreshUrl = (if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/") + "auth/refresh"
            val request = Request.Builder()
                .url(refreshUrl)
                .post("".toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()))
                .build()

            getRefreshClient().newCall(request).execute().use { resp ->
                if (!resp.isSuccessful) return false

                val bodyString = resp.body.string()

                if (bodyString.isBlank()) return false

                val refreshResponseData = try {
                    Gson().fromJson(bodyString, RefreshResponseDto::class.java)
                } catch (_: Exception) {
                    null
                }
                val newAccess = refreshResponseData?.accessToken
                val newCsrf = refreshResponseData?.csrfToken

                if (!newAccess.isNullOrBlank()) {
                    tokenRepo.saveTokens(newAccess, newCsrf)
                    true
                } else false
            }
        } catch (_: IOException) {
            false
        }
    }

    private fun getRefreshClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}

