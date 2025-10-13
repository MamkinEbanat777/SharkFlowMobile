package com.example.sharkflow.data.repository

import com.example.sharkflow.data.api.*
import com.example.sharkflow.model.*
import com.example.sharkflow.utils.*
import jakarta.inject.*
import retrofit2.*

class RegisterRepository @Inject constructor(
    private val authApi: AuthApi
) {
    suspend fun register(
        login: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<Unit> {
        return try {
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
    }

    suspend fun confirmCode(code: String): Response<Unit> {
        val request = ConfirmationCodeRequest(code)
        return authApi.confirmationCode(request)
    }
}
