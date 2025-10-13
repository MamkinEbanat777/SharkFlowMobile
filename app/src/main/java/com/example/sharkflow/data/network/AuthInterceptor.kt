package com.example.sharkflow.data.network

import com.example.sharkflow.data.repository.*
import com.example.sharkflow.model.*
import com.google.gson.*
import jakarta.inject.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.util.concurrent.*
import java.util.concurrent.locks.*
import kotlin.concurrent.*
import kotlin.jvm.Volatile

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenRepo: TokenRepository,
    private val baseUrl: String
) : Interceptor {

    private val lock = ReentrantLock()
    private val refreshComplete = lock.newCondition()

    @Volatile
    private var isRefreshing = false

    private val RETRY_HEADER = "X-Auth-Retry"

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val (accessToken, csrfToken) = tokenRepo.loadTokens()
        if (!accessToken.isNullOrBlank()) {
            request = request.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .apply {
                    csrfToken?.let { header("X-CSRF-TOKEN", it) }
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
                                    .header("Authorization", "Bearer $newAccess")
                                    .apply { newCsrf?.let { header("X-CSRF-TOKEN", it) } }
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
                            .header("Authorization", "Bearer $newAccess")
                            .apply { newCsrf?.let { header("X-CSRF-TOKEN", it) } }
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
                .post(ByteArray(0).toRequestBody(null, 0, 0))
                .build()

            getRefreshClient().newCall(request).execute().use { resp ->
                if (!resp.isSuccessful) return false

                val bodyString = resp.body.string()
                if (bodyString.isBlank()) return false

                val refreshData = try {
                    Gson().fromJson(bodyString, Refresh::class.java)
                } catch (_: Exception) {
                    null
                }
                val newAccess = refreshData?.accessToken
                val newCsrf = refreshData?.csrfToken

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

