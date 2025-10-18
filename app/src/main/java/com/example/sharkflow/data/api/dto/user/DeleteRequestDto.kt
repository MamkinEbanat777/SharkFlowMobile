package com.example.sharkflow.data.api.dto.user

import com.google.gson.annotations.SerializedName

data class DeleteUserRequestDto(
    @SerializedName("confirmationCode")
    val confirmationCode: String?
)
