package com.example.sharkflow.data.api.dto.common

import com.google.gson.annotations.SerializedName

data class GenericMessageResponseDto(
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null
)