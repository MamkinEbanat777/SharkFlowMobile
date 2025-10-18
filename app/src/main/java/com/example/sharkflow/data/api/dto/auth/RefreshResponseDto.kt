package com.example.sharkflow.data.api.dto.auth

import com.google.gson.annotations.SerializedName

data class RefreshResponseDto(
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("csrfToken") val csrfToken: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("error") val error: String?
)
