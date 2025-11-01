package com.example.sharkflow.data.network

import com.example.sharkflow.domain.manager.TokenManager
import jakarta.inject.Inject
import okhttp3.*

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val X_CSRF_HEADER = "X-CSRF-TOKEN"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val access = tokenManager.getCurrentAccessToken()
        val csrf = tokenManager.getCurrentCsrfToken()
        val newReq = if (!access.isNullOrBlank()) {
            request.newBuilder()
                .header(AUTHORIZATION_HEADER, "Bearer $access")
                .apply { csrf?.let { header(X_CSRF_HEADER, it) } }
                .build()
        } else {
            request
        }
        return chain.proceed(newReq)
    }
}
