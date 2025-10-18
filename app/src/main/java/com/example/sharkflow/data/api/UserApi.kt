package com.example.sharkflow.data.api

import com.example.sharkflow.domain.model.*
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @GET("users")
    suspend fun getUser(): Response<UserResponse>

    @POST("users/delete/confirm-deletion")
    suspend fun confirmationDeleteUser(): Response<ConfirmationDeleteUserResponse>

    @POST("users/delete")
    suspend fun deleteUser(@Body request: DeleteUserRequest): Response<DeleteUserResponse>

    @POST("users/confirm-update")
    suspend fun confirmationUpdateUser(): Response<ConfirmationUpdateUserResponse>

    @PATCH("users")
    suspend fun updateUser(@Body request: UpdateUserRequest): Response<UpdateUserResponse>

    @PATCH("users/avatar")
    suspend fun updateUserAvatar(@Body request: UpdateAvatarRequest): Response<UpdateAvatarResponse>

    @DELETE("users/avatar")
    suspend fun deleteUserAvatar(): Response<DeleteAvatarResponse>

    @POST("users/confirm-registration")
    suspend fun register(@Body request: RegisterUser): Response<RegisterResponse>

    @POST("users")
    suspend fun confirmationCode(@Body request: ConfirmationCodeRequest): Response<Unit>
}