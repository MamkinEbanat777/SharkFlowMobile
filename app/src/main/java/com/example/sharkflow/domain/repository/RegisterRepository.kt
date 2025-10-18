package com.example.sharkflow.domain.repository

import com.example.sharkflow.data.api.dto.common.GenericMessageResponseDto
import retrofit2.Response

interface RegisterRepository {
    suspend fun register(
        login: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<Unit>

    suspend fun confirmCode(code: String): Response<GenericMessageResponseDto>
}
