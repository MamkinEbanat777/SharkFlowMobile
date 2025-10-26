package com.example.sharkflow.domain.usecase.auth

import com.example.sharkflow.data.api.dto.common.GenericMessageResponseDto
import com.example.sharkflow.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutFromDeviceUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(deviceId: String): Result<GenericMessageResponseDto> {
        return authRepository.logoutFromDevice(deviceId)
    }
}