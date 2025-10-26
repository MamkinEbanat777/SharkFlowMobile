package com.example.sharkflow.data.api.dto.user

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class GeoLocationDto(
    @SerializedName("ip")
    val ip: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)
