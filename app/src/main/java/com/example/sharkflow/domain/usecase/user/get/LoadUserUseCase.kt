package com.example.sharkflow.domain.usecase.user.get

import com.example.sharkflow.data.repository.combined.UserRepositoryCombined
import com.example.sharkflow.domain.model.User
import javax.inject.Inject

class LoadUserUseCase @Inject constructor(
    private val userRepositoryCombined: UserRepositoryCombined
) {
    suspend operator fun invoke(): Result<User> {
        return userRepositoryCombined.loadUser()
    }
}