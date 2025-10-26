package com.example.sharkflow.data.repository.remote

import com.example.sharkflow.data.api.UserApi
import com.example.sharkflow.data.api.dto.user.*
import com.example.sharkflow.data.mapper.*
import com.example.sharkflow.domain.model.*
import com.example.sharkflow.domain.repository.UserRepository
import com.example.sharkflow.utils.safeApiCall
import javax.inject.*

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
) : UserRepository {

    override suspend fun loadUser(): Result<User> {
        return safeApiCall { userApi.getUser() }
            .mapCatching { dto -> UserMapper.fromDto(dto) }
    }

    override suspend fun requestUpdateUserCode(): Result<String> {
        return safeApiCall { userApi.confirmationUpdateUser() }.map {
            it.message ?: "Код отправлен"
        }
    }

    override suspend fun confirmUpdateUser(
        code: String,
        email: String,
        login: String
    ): Result<UpdateUserResponseDto> {
        val fieldsDto = UpdatedUserFieldsDto(email = email, login = login)
        val request = UpdateUserRequestDto(confirmationCode = code, updatedFields = fieldsDto)
        return safeApiCall { userApi.updateUser(request) }
    }

    override suspend fun requestDeleteUserCode(): Result<String> {
        return safeApiCall { userApi.confirmationDeleteUser() }.map {
            it.message ?: "Код отправлен"
        }
    }

    override suspend fun confirmDeleteUser(code: String): Result<String> {
        val request = DeleteUserDto(confirmationCode = code)
        return safeApiCall { userApi.deleteUser(request) }.map {
            it.message ?: "Пользователь удалён"
        }
    }

    override suspend fun updateUserAvatar(
        avatarUrl: String,
        publicId: String
    ): Result<UpdateUserAvatarResponseDto> {
        val request = UpdateUserAvatarRequestDto(imgUrl = avatarUrl, publicId = publicId)
        return safeApiCall { userApi.updateUserAvatar(request) }
    }


    override suspend fun deleteUserAvatar(): Result<String> {
        return safeApiCall { userApi.deleteUserAvatar() }.map { it.message ?: "Аватар удалён" }
    }

    override suspend fun loadUserSessions(): Result<List<UserSession>> {
        return safeApiCall { userApi.loadUserDevices() }
            .mapCatching { wrapperDto ->
                UserSessionMapper.fromWrapper(wrapperDto)
            }
    }


}

