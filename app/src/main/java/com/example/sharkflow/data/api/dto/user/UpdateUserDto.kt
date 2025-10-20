package com.example.sharkflow.data.api.dto.user

import com.example.sharkflow.domain.model.User
import com.google.gson.annotations.SerializedName

data class UpdatedUserFieldsDto(
    @SerializedName("email") val email: String? = null,
    @SerializedName("login") val login: String? = null
)

data class UpdateUserRequestDto(
    @SerializedName("confirmationCode") val confirmationCode: String,
    @SerializedName("updatedFields") val updatedFields: UpdatedUserFieldsDto? = null
)

data class UpdateUserResponseDto(
    val message: String,
    val user: User
)

