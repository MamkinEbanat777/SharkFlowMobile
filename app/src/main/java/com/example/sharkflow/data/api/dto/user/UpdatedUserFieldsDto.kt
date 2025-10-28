package com.example.sharkflow.data.api.dto.user

import com.google.gson.annotations.SerializedName

data class UpdatedUserFieldsDto(
    @SerializedName("email") val email: String? = null,
    @SerializedName("login") val login: String? = null
)