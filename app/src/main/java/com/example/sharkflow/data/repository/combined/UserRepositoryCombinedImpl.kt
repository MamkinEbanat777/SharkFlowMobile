package com.example.sharkflow.data.repository.combined

import com.example.sharkflow.data.api.dto.user.*
import com.example.sharkflow.data.repository.local.UserLocalRepositoryImpl
import com.example.sharkflow.data.repository.remote.UserRepositoryImpl
import com.example.sharkflow.domain.manager.UserManager
import com.example.sharkflow.domain.model.User
import com.example.sharkflow.domain.repository.UserRepositoryCombined
import jakarta.inject.*

@Singleton
class UserRepositoryCombinedImpl @Inject constructor(
    private val local: UserLocalRepositoryImpl,
    private val remote: UserRepositoryImpl,
    private val userManager: UserManager
) : UserRepositoryCombined {
    override suspend fun loadUser(): Result<User> {
        return remote.loadUser()
            .mapCatching { user ->
                local.setActiveUser(user)
                user
            }.recoverCatching { e ->
                val cachedUser = local.getActiveUserOnce()
                cachedUser ?: throw e
            }
    }

    override suspend fun requestUpdateUserCode(): Result<String> {
        return remote.requestUpdateUserCode()
    }

    override suspend fun confirmUpdateUser(
        code: String,
        email: String,
        login: String
    ): Result<UpdateUserResponseDto> {
        val result = remote.confirmUpdateUser(code, email, login)
        result.onSuccess { updateResponse ->
            updateResponse.user.let { user ->
                local.insertOrUpdate(user)
            }
        }
        return result
    }

    override suspend fun requestDeleteUserCode(): Result<String> {
        return remote.requestDeleteUserCode()
    }

    override suspend fun confirmDeleteUser(code: String): Result<String> {
        val result = remote.confirmDeleteUser(code)
        result.onSuccess {
            local.clearAll()
        }
        return result
    }

    override suspend fun updateUserAvatar(
        avatarUrl: String,
        publicId: String
    ): Result<UpdateUserAvatarResponseDto> {
        val result = remote.updateUserAvatar(avatarUrl, publicId)
        result.onSuccess { response ->
            userManager.currentUser.value?.let { user ->
                val updatedUser = user.copy(
                    avatarUrl = response.avatarUrl,
                    publicId = publicId
                )
                local.insertOrUpdate(updatedUser)
            }
        }
        return result
    }

    override suspend fun deleteUserAvatar(): Result<String> {
        val result = remote.deleteUserAvatar()
        result.onSuccess {
            userManager.currentUser.value?.let { user ->
                val updatedUser = user.copy(
                    avatarUrl = null,
                    publicId = null
                )
                local.insertOrUpdate(updatedUser)
            }
        }
        return result
    }

    override fun getUserFlow(uuid: String) = local.getUserFlow(uuid)
}
