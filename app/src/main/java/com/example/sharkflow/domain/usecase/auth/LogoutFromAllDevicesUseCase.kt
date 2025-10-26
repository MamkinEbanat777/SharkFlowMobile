package com.example.sharkflow.domain.usecase.auth

import com.example.sharkflow.data.api.dto.common.GenericMessageResponseDto
import com.example.sharkflow.data.repository.local.UserLocalRepositoryImpl
import com.example.sharkflow.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutFromAllDevicesUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val local: UserLocalRepositoryImpl
) {
    suspend operator fun invoke(): Result<GenericMessageResponseDto> {
        local.clearAll()
        return authRepository.logoutFromAllDevices()
    }
}