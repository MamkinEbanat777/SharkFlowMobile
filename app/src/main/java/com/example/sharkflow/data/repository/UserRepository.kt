package com.example.sharkflow.data.repository

import com.example.sharkflow.data.api.UserApi
import com.example.sharkflow.model.*
import kotlinx.coroutines.flow.*
import javax.inject.*

@Singleton
class UserRepository @Inject constructor(private val userApi: UserApi) {
    private val _currentUser = MutableStateFlow<UserResponse?>(null)
    val currentUser: StateFlow<UserResponse?> = _currentUser

    fun setUser(user: UserResponse?) {
        _currentUser.value = user
    }

    fun clearUser() {
        _currentUser.value = null
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
            if (response.isSuccessful) clearUser()
            Result.success(response.body()?.message ?: "Пользователь удалён")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loadUser(): Boolean {
        return try {
            val response = userApi.getUser()
            if (response.isSuccessful) {
                _currentUser.value = response.body()
                true
            } else {
                clearUser()
                false
            }
        } catch (e: Exception) {
            clearUser()
            false
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
                val currentUser = _currentUser.value
                if (currentUser != null) {
                    _currentUser.value = currentUser.copy(avatarUrl = avatarUrl)
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
                val currentUser = _currentUser.value
                if (currentUser != null) {
                    _currentUser.value = currentUser.copy(avatarUrl = "")
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
