package com.example.sharkflow.data.repository

import com.example.sharkflow.data.api.AuthApi
import com.example.sharkflow.data.manager.RegisterManager
import com.example.sharkflow.model.*
import com.example.sharkflow.utils.ErrorMapper
import jakarta.inject.Inject
import retrofit2.Response

class RegisterRepository @Inject constructor(
    private val authApi: AuthApi,
    private val registerManager: RegisterManager
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

            val response = authApi.register(user)

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
        result.onSuccess { registerManager.setRegistered(true) }
        return result
    }

    suspend fun confirmCode(code: String): Response<ConfirmationCodeResponse> {
        val request = ConfirmationCodeRequest(code)
        return authApi.confirmationCode(request)
    }
}
