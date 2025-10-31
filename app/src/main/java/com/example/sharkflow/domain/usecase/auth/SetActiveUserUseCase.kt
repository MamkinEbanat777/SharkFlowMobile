package com.example.sharkflow.domain.usecase.auth

import com.example.sharkflow.data.repository.local.UserLocalRepositoryImpl
import com.example.sharkflow.domain.manager.UserManager
import com.example.sharkflow.domain.model.User
import jakarta.inject.Inject

class SetActiveUserUseCase @Inject constructor(
    private val local: UserLocalRepositoryImpl,
    private val userManager: UserManager
) {
    suspend operator fun invoke(user: User) {
        local.setActiveUser(user)
        userManager.setUser(user)
    }
}
