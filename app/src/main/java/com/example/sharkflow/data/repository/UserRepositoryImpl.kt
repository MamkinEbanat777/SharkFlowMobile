package com.example.sharkflow.data.repository

import com.example.sharkflow.data.api.UserApi
import com.example.sharkflow.data.api.dto.user.*
import com.example.sharkflow.data.manager.UserManager
import com.example.sharkflow.data.mapper.UserMapper
import com.example.sharkflow.domain.model.User
import com.example.sharkflow.domain.repository.UserRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.*

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val userManager: UserManager
) : UserRepository {

    override val currentUser: StateFlow<User?> = userManager.currentUser

    override fun setUser(user: User?) {
        userManager.setUser(user)
    }

    override fun clearUser() {
        userManager.clearUser()
    }

    override suspend fun loadUser(): Result<User> {
        return try {
            val response = userApi.getUser()
            if (response.isSuccessful) {
                val bodyDto = response.body()
                if (bodyDto != null) {
                    val user = UserMapper.fromDto(bodyDto)
                    userManager.setUser(user)
                    Result.success(user)
                } else {
                    Result.failure(Exception("Не удалось получить пользователя"))
                }
            } else {
                userManager.clearUser()
                Result.failure(Exception("Ошибка получения данных: ${response.code()}"))
            }
        } catch (e: Exception) {
            userManager.clearUser()
            Result.failure(e)
        }
    }

    override suspend fun requestUpdateUserCode(): Result<String> {
        return try {
            val response = userApi.confirmationUpdateUser()
            if (response.isSuccessful) {
                val msg = response.body()?.message ?: "Код отправлен"
                Result.success(msg)
            } else {
                Result.failure(Exception("Ошибка: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun confirmUpdateUser(
        code: String,
        email: String,
        login: String
    ): Result<String> {
        return try {
            val fieldsDto = UpdatedFieldsDto(email = email, login = login)
            val request = UpdateUserRequestDto(confirmationCode = code, updatedFields = fieldsDto)
            val response = userApi.updateUser(request)
            if (response.isSuccessful) {
                loadUser()
                Result.success(response.body()?.message ?: "Пользователь обновлён")
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Ошибка: ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun requestDeleteUserCode(): Result<String> {
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

    override suspend fun confirmDeleteUser(code: String): Result<String> {
        return try {
            val request = DeleteUserRequestDto(confirmationCode = code)
            val response = userApi.deleteUser(request)
            if (response.isSuccessful) {
                userManager.clearUser()
                Result.success(response.body()?.message ?: "Пользователь удалён")
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Ошибка: ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserAvatar(avatarUrl: String, publicId: String): Result<String> {
        return try {
            val request = UpdateAvatarRequestDto(imgUrl = avatarUrl, publicId = publicId)
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

    override suspend fun deleteUserAvatar(): Result<String> {
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
