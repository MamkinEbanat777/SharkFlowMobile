package com.example.sharkflow.domain.usecase.user.get

import com.example.sharkflow.domain.manager.UserManager
import com.example.sharkflow.domain.model.User
import com.example.sharkflow.domain.repository.UserRepositoryCombined
import javax.inject.Inject

class LoadUserUseCase @Inject constructor(
    private val userRepositoryCombined: UserRepositoryCombined,
    private val userManager: UserManager
) {
    suspend operator fun invoke(): Result<User> {
        return userRepositoryCombined.loadUser(userManager.currentUser.value?.uuid ?: "")
    }
}