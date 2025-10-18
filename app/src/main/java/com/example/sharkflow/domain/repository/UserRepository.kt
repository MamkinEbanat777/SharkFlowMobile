package com.example.sharkflow.domain.repository

import com.example.sharkflow.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val currentUser: StateFlow<User?>
    fun setUser(user: User?)
    fun clearUser()

    suspend fun loadUser(): Result<User>
    suspend fun requestUpdateUserCode(): Result<String>
    suspend fun confirmUpdateUser(code: String, email: String, login: String): Result<String>
    suspend fun requestDeleteUserCode(): Result<String>
    suspend fun confirmDeleteUser(code: String): Result<String>
    suspend fun updateUserAvatar(avatarUrl: String, publicId: String): Result<String>
    suspend fun deleteUserAvatar(): Result<String>
}
