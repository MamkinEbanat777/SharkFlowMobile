package com.example.sharkflow.data.api.dto.user

import com.google.gson.annotations.SerializedName

data class UpdateUserRequestDto(
    @SerializedName("confirmationCode") val confirmationCode: String,
    @SerializedName("updatedFields") val updatedFields: UpdatedUserFieldsDto? = null
)