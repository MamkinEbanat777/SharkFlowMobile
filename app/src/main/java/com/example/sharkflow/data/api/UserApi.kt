package com.example.sharkflow.data.api

import com.example.sharkflow.model.*
import retrofit2.*
import retrofit2.http.*

interface UserApi {
    @GET("users")
    suspend fun getUser(): Response<UserResponse>

    @POST("auth/logout")
    suspend fun logoutUser(): Response<LogoutResponse>
}