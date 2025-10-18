package com.example.sharkflow.data.api

import com.example.sharkflow.data.api.dto.auth.*
import com.example.sharkflow.data.api.dto.common.GenericMessageResponseDto
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginUserDto): Response<LoginResponseDto>

    @POST("auth/logout")
    suspend fun logout(): Response<GenericMessageResponseDto>
}
