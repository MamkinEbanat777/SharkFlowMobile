package com.example.sharkflow.data.api

import com.example.sharkflow.domain.model.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginUser): Response<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<LogoutResponse>
}
