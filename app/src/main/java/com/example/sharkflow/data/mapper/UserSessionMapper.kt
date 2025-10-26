package com.example.sharkflow.data.mapper

import com.example.sharkflow.data.api.dto.user.*
import com.example.sharkflow.domain.model.UserSession

object UserSessionMapper {
    fun fromDto(dto: LoadUserSessionsResponseDto): UserSession = UserSession(
        deviceId = dto.deviceId,
        ipAddress = dto.ipAddress,
        geoLocation = dto.geoLocation?.ip,
        deviceType = dto.deviceType,
        deviceBrand = dto.deviceBrand,
        deviceModel = dto.deviceModel,
        osName = dto.osName,
        osVersion = dto.osVersion,
        clientName = dto.clientName,
        clientVersion = dto.clientVersion,
        userAgent = dto.userAgent,
        createdAt = dto.createdAt,
        lastLoginAt = dto.lastLoginAt,
        lastUsedAt = dto.lastUsedAt,
        isActive = dto.isActive
    )

    fun fromWrapper(wrapper: LoadUserSessionsWrapperDto): List<UserSession> =
        wrapper.devices.map { fromDto(it) }
}
