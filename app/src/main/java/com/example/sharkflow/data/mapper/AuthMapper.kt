package com.example.sharkflow.data.mapper

import com.example.sharkflow.data.api.dto.auth.*
import com.example.sharkflow.domain.model.*

object AuthMapper {
    fun toLoginUserDto(params: LoginParams): LoginUserDto {
        return LoginUserDto(
            user = LoginRequestDto(
                email = params.email,
                password = params.password,
                rememberMe = params.rememberMe
            )
        )
    }

    fun fromLoginResponse(dto: LoginResponseDto): AuthToken? {
        val access = dto.accessToken
        val csrf = dto.csrfToken
        return if (!access.isNullOrBlank() && !csrf.isNullOrBlank()) {
            AuthToken(accessToken = access, csrfToken = csrf, deviceId = dto.deviceId)
        } else {
            throw IllegalArgumentException("Invalid login response: missing tokens")
        }
    }

    fun fromRefreshResponse(dto: RefreshResponseDto): AuthToken? {
        val access = dto.accessToken
        val csrf = dto.csrfToken
        return if (!access.isNullOrBlank() && !csrf.isNullOrBlank()) {
            AuthToken(accessToken = access, csrfToken = csrf, deviceId = null)
        } else {
            null
        }
    }
}
