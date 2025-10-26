package com.example.sharkflow.domain.usecase.user.get

import com.example.sharkflow.domain.model.UserSession
import com.example.sharkflow.domain.repository.UserRepository
import javax.inject.Inject

class LoadUserSessionsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<List<UserSession>> {
        return userRepository.loadUserSessions()
    }
}
