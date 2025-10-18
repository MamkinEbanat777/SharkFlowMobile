package com.example.sharkflow.data.api.dto.auth

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("accessToken") val accessToken: String? = null,
    @SerializedName("csrfToken") val csrfToken: String? = null,
    @SerializedName("deviceId") val deviceId: String? = null,
    @SerializedName("githubOAuthEnabled") val githubOAuthEnabled: Boolean? = null,
    @SerializedName("error") val error: String? = null
)
