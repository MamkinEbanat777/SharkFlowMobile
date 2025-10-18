package com.example.sharkflow.domain.repository

import com.example.sharkflow.data.api.UserApi
import com.example.sharkflow.data.service.UserManager
import com.example.sharkflow.domain.model.*
import kotlinx.coroutines.flow.StateFlow
import javax.inject.*

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val userManager: UserManager
) {
    val currentUser: StateFlow<UserResponse?> = userManager.currentUser

    fun setUser(user: UserResponse?) {
        userManager.setUser(user)
    }

    fun clearUser() {
        userManager.clearUser()
    }

    suspend fun requestUpdateUserCode(): Result<String> {
        return try {
            val response = userApi.confirmationUpdateUser()
            if (response.isSuccessful) {
                Result.success(response.body()?.message ?: "Код отправлен")
            } else {
                Result.failure(Exception("Ошибка: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun confirmUpdateUser(
        code: String,
        email: String,
        login: String
    ): Result<String> {
        return try {
            val request = UpdateUserRequest(
                confirmationCode = code,
                updatedFields = UpdatedFields(email = email, login = login)
            )
            val response = userApi.updateUser(request)
            if (response.isSuccessful) {
                loadUser()
                Result.success(response.body()?.message ?: "Пользователь обновлён")
            } else {
                Result.failure(Exception(response.body()?.message ?: "Ошибка: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun confirmDeleteUser(code: String): Result<String> {
        return try {
            val request = DeleteUserRequest(confirmationCode = code)
            val response = userApi.deleteUser(request)
            if (response.isSuccessful) userManager.clearUser()
            Result.success(response.body()?.message ?: "Пользователь удалён")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loadUser(): Result<UserResponse> {
        return try {
            val response = userApi.getUser()
            if (response.isSuccessful) {
                response.body()?.let {
                    userManager.setUser(it)
                    Result.success(it)
                } ?: Result.failure(Exception("Не удалось получить пользователя"))
            } else {
                userManager.clearUser()
                Result.failure(Exception("Ошибка получения данных: ${response.code()}"))
            }
        } catch (e: Exception) {
            userManager.clearUser()
            Result.failure(e)
        }
    }


    suspend fun requestDeleteUserCode(): Result<String> {
        return try {
            val response = userApi.confirmationDeleteUser()
            if (response.isSuccessful) {
                Result.success(response.body()?.message ?: "Код отправлен")
            } else {
                Result.failure(Exception("Ошибка: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserAvatar(avatarUrl: String, publicId: String): Result<String> {
        return try {
            val request = UpdateAvatarRequest(imgUrl = avatarUrl, publicId = publicId)
            val response = userApi.updateUserAvatar(request)

            if (response.isSuccessful) {
                userManager.currentUser.value?.let {
                    userManager.setUser(it.copy(avatarUrl = avatarUrl))
                }
                Result.success(response.body()?.message ?: "Аватар обновлён")
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Неизвестная ошибка"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUserAvatar(): Result<String> {
        return try {
            val response = userApi.deleteUserAvatar()

            if (response.isSuccessful) {
                userManager.currentUser.value?.let {
                    userManager.setUser(it.copy(avatarUrl = ""))
                }
                Result.success(response.body()?.message ?: "Аватар удалён")
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Неизвестная ошибка"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
