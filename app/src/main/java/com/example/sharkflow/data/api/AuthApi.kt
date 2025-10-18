package com.example.sharkflow.data.api

import com.example.sharkflow.model.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    @POST("users/confirm-registration")
    suspend fun register(@Body request: RegisterUser): Response<RegisterResponse>

    @POST("users")
    suspend fun confirmationCode(@Body request: ConfirmationCodeRequest): Response<ConfirmationCodeResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginUser): Response<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<LogoutResponse>
}
