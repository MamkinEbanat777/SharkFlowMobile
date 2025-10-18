package com.example.sharkflow.data.api

import com.example.sharkflow.data.api.dto.auth.RegisterUserDto
import com.example.sharkflow.data.api.dto.common.GenericMessageResponseDto
import com.example.sharkflow.data.api.dto.user.*
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @GET("users")
    suspend fun getUser(): Response<UserResponseDto>

    @POST("users/confirm-registration")
    suspend fun register(@Body request: RegisterUserDto): Response<GenericMessageResponseDto>

    @POST("users")
    suspend fun confirmationCode(@Body request: ConfirmationCodeRequestDto): Response<ConfirmationCodeResponseDto>

    @POST("users/delete/confirm-deletion")
    suspend fun confirmationDeleteUser(): Response<GenericMessageResponseDto>

    @POST("users/delete")
    suspend fun deleteUser(@Body request: DeleteUserRequestDto): Response<GenericMessageResponseDto>

    @POST("users/confirm-update")
    suspend fun confirmationUpdateUser(): Response<GenericMessageResponseDto>

    @PATCH("users")
    suspend fun updateUser(@Body request: UpdateUserRequestDto): Response<GenericMessageResponseDto>

    @PATCH("users/avatar")
    suspend fun updateUserAvatar(@Body request: UpdateAvatarRequestDto): Response<GenericMessageResponseDto>

    @DELETE("users/avatar")
    suspend fun deleteUserAvatar(): Response<GenericMessageResponseDto>
}
