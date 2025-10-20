package com.example.sharkflow.domain.usecase.user.get

import com.example.sharkflow.domain.model.User
import com.example.sharkflow.domain.repository.UserRepository
import javax.inject.Inject

class LoadUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<User> {
        return userRepository.loadUser()
    }
}