package com.example.sharkflow.data.api

import com.example.sharkflow.model.*
import retrofit2.*
import retrofit2.http.*

interface AuthApi {
    @POST("users/confirm-registration")
    suspend fun register(@Body request: RegisterUser): Response<RegisterResponse>

    @POST("users")
    suspend fun confirmationCode(@Body request: ConfirmationCodeRequest): Response<Unit>

    @POST("auth/login")
    suspend fun login(@Body request: LoginUser): Response<LoginResponse>
}
