package com.example.sharkflow.data.api

import com.example.sharkflow.data.api.dto.auth.RegisterUserDto
import com.example.sharkflow.data.api.dto.common.GenericMessageResponseDto
import com.example.sharkflow.data.api.dto.user.*
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @GET("users")
    suspend fun getUser(): Response<UserDto>

    @POST("users/confirm-registration")
    suspend fun register(@Body request: RegisterUserDto): Response<GenericMessageResponseDto>

    @POST("users")
    suspend fun confirmationCode(@Body request: ConfirmationCodeRequestDto): Response<GenericMessageResponseDto>

    @POST("users/delete/confirm-deletion")
    suspend fun confirmationDeleteUser(): Response<GenericMessageResponseDto>

    @POST("users/delete")
    suspend fun deleteUser(@Body request: DeleteUserDto): Response<GenericMessageResponseDto>

    @POST("users/confirm-update")
    suspend fun confirmationUpdateUser(): Response<GenericMessageResponseDto>

    @PATCH("users")
    suspend fun updateUser(@Body request: UpdateUserRequestDto): Response<UpdateUserResponseDto>

    @PATCH("users/avatar")
    suspend fun updateUserAvatar(@Body request: UpdateUserAvatarRequestDto): Response<UpdateUserAvatarResponseDto>

    @DELETE("users/avatar")
    suspend fun deleteUserAvatar(): Response<GenericMessageResponseDto>

    @GET("users/devices")
    suspend fun loadUserDevices(): Response<LoadUserSessionsWrapperDto>

}
