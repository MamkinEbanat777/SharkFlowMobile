package com.example.sharkflow.data.network

import com.example.sharkflow.domain.repository.TokenRepository
import jakarta.inject.Inject
import okhttp3.*

class AuthInterceptor @Inject constructor(private val tokenRepo: TokenRepository) : Interceptor {
    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val X_CSRF_HEADER = "X-CSRF-TOKEN"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val (accessToken, csrfToken) = tokenRepo.loadTokens()
        if (!accessToken.isNullOrBlank()) {
            request = request.newBuilder()
                .header(AUTHORIZATION_HEADER, "Bearer $accessToken")
                .apply { csrfToken?.let { header(X_CSRF_HEADER, it) } }
                .build()
        }
        return chain.proceed(request)
    }
}
