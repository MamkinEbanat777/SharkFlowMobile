package com.example.sharkflow.data.api.dto.user

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("uuid") val uuid: String,
    @SerializedName("login") val login: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("publicId") val publicId: String?
)
