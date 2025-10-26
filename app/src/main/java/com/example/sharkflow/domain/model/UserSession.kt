package com.example.sharkflow.domain.model

data class UserSession(
    val deviceId: String,
    val ipAddress: String?,
    val geoLocation: String?,
    val deviceType: String?,
    val deviceBrand: String?,
    val deviceModel: String?,
    val osName: String?,
    val osVersion: String?,
    val clientName: String?,
    val clientVersion: String?,
    val userAgent: String?,
    val createdAt: String,
    val lastLoginAt: String,
    val lastUsedAt: String?,
    val isActive: Boolean
)
