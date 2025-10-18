package com.example.sharkflow.data.api.dto.user

import com.google.gson.annotations.SerializedName

data class UpdatedFieldsDto(
    @SerializedName("email") val email: String? = null,
    @SerializedName("login") val login: String? = null
)

data class UpdateUserRequestDto(
    @SerializedName("confirmationCode") val confirmationCode: String,
    @SerializedName("updatedFields") val updatedFields: UpdatedFieldsDto? = null
)
