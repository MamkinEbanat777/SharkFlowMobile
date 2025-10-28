package com.example.sharkflow.data.api.dto.user

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LoadUserSessionsWrapperDto(
    @SerializedName("devices")
    val devices: List<LoadUserSessionsResponseDto>
)