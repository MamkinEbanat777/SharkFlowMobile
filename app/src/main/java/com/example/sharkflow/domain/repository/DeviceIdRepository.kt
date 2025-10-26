package com.example.sharkflow.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface DeviceIdRepository {
    fun deviceIdFlow(): StateFlow<String>
    fun getOrCreateDeviceId(): String
}
