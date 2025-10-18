package com.example.sharkflow.domain.repository

import com.example.sharkflow.data.api.UserApi
import com.example.sharkflow.data.service.RegisterService
import com.example.sharkflow.domain.model.*
import com.example.sharkflow.utils.ErrorMapper
import jakarta.inject.Inject
import retrofit2.Response

class RegisterRepository @Inject constructor(
    private val userApi: UserApi,
    private val registerService: RegisterService
) {
    suspend fun register(
        login: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<Unit> {
        val result = try {
            val request = RegisterRequest(login, email, password, confirmPassword)
            val user = RegisterUser(user = request)

            val response = userApi.register(user)

            if (response.isSuccessful) {
                Result.success(Unit)

            } else {
                val errorBodyString = response.errorBody()?.string()
                val errorMessage = ErrorMapper.map(response.code(), errorBodyString)
                Result.failure(Exception(errorMessage))
            }
        } catch (_: Exception) {
            Result.failure(Exception("Сервер недоступен, попробуйте позже"))
        }
        result.onSuccess { registerService.setRegistered(true) }
        return result
    }

    suspend fun confirmCode(code: String): Response<Unit> {
        val request = ConfirmationCodeRequest(code)
        return userApi.confirmationCode(request)
    }
}
