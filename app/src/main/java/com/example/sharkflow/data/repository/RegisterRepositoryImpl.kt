package com.example.sharkflow.data.repository

import com.example.sharkflow.data.api.UserApi
import com.example.sharkflow.data.api.dto.auth.*
import com.example.sharkflow.data.api.dto.user.ConfirmationCodeRequestDto
import com.example.sharkflow.domain.repository.RegisterRepository
import com.example.sharkflow.utils.safeApiCall
import jakarta.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
) : RegisterRepository {
    override suspend fun register(
        login: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<Unit> {
        val requestDto = RegisterRequestDto(
            login = login,
            email = email,
            password = password,
            confirmPassword = confirmPassword
        )
        val userDto = RegisterUserDto(user = requestDto)

        return safeApiCall { userApi.register(userDto) }.map { Unit }
    }

    override suspend fun confirmCode(code: String): Result<String> {
        val requestDto = ConfirmationCodeRequestDto(code)
        return safeApiCall { userApi.confirmationCode(requestDto) }
            .map { dto -> dto.message ?: "OK" }

    }
}