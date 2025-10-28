package com.example.sharkflow.data.api.dto.user

import com.google.gson.annotations.SerializedName

data class LoadUserSessionsResponseDto(
    @SerializedName("deviceId")
    val deviceId: String,

    @SerializedName("ipAddress")
    val ipAddress: String?,

    @SerializedName("geoLocation")
    val geoLocation: GeoLocationDto?,

    @SerializedName("deviceType")
    val deviceType: String?,

    @SerializedName("deviceBrand")
    val deviceBrand: String?,

    @SerializedName("deviceModel")
    val deviceModel: String?,

    @SerializedName("osName")
    val osName: String?,

    @SerializedName("osVersion")
    val osVersion: String?,

    @SerializedName("clientName")
    val clientName: String?,

    @SerializedName("clientVersion")
    val clientVersion: String?,

    @SerializedName("userAgent")
    val userAgent: String?,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("lastLoginAt")
    val lastLoginAt: String,

    @SerializedName("lastUsedAt")
    val lastUsedAt: String?,

    @SerializedName("isActive")
    val isActive: Boolean
)