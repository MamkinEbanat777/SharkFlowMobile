package com.example.sharkflow.domain.usecase.auth

import com.example.sharkflow.domain.repository.DeviceIdRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.StateFlow

class DeviceIdUseCase @Inject constructor(
    private val deviceIdRepository: DeviceIdRepository
) {
    operator fun invoke(): StateFlow<String> {
        return deviceIdRepository.deviceIdFlow()
    }
}

