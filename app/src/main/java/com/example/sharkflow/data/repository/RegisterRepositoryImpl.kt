package com.example.sharkflow.data.repository

import com.example.sharkflow.data.api.UserApi
import com.example.sharkflow.data.api.dto.auth.*
import com.example.sharkflow.data.api.dto.common.GenericMessageResponseDto
import com.example.sharkflow.data.api.dto.user.ConfirmationCodeRequestDto
import com.example.sharkflow.data.manager.RegisterManager
import com.example.sharkflow.domain.repository.RegisterRepository
import com.example.sharkflow.utils.ErrorMapper
import jakarta.inject.Inject
import retrofit2.Response
import javax.inject.Singleton

@Singleton
class RegisterRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val registerManager: RegisterManager
) : RegisterRepository {
    override suspend fun register(
        login: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<Unit> {
        val result = try {
            val requestDto = RegisterRequestDto(
                login = login,
                email = email,
                password = password,
                confirmPassword = confirmPassword
            )
            val userDto = RegisterUserDto(user = requestDto)

            val response = userApi.register(userDto)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBodyString = response.errorBody()?.string()
                val errorMessage = ErrorMapper.map(response.code(), errorBodyString)
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Сервер недоступен, попробуйте позже: ${e.message}"))
        }

        result.onSuccess { registerManager.setRegistered(true) }
        return result
    }

    override suspend fun confirmCode(code: String): Response<GenericMessageResponseDto> {
        val requestDto = ConfirmationCodeRequestDto(code)
        return userApi.confirmationCode(requestDto)
    }
}