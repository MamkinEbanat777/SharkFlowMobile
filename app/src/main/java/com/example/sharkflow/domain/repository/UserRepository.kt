package com.example.sharkflow.domain.repository

import com.example.sharkflow.data.api.dto.user.*
import com.example.sharkflow.domain.model.User

interface UserRepository {
    suspend fun loadUser(): Result<User>
    suspend fun requestUpdateUserCode(): Result<String>
    suspend fun confirmUpdateUser(
        code: String,
        email: String,
        login: String
    ): Result<UpdateUserResponseDto>

    suspend fun requestDeleteUserCode(): Result<String>
    suspend fun confirmDeleteUser(code: String): Result<String>
    suspend fun updateUserAvatar(
        avatarUrl: String,
        publicId: String
    ): Result<UpdateUserAvatarResponseDto>

    suspend fun deleteUserAvatar(): Result<String>
}
