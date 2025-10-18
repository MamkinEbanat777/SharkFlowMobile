package com.example.sharkflow.data.api.dto.user

import com.example.sharkflow.data.api.dto.common.GenericMessageResponseDto
import com.google.gson.annotations.SerializedName

data class ConfirmationCodeRequestDto(
    @SerializedName("confirmationCode") val confirmationCode: String?
)

typealias ConfirmationCodeResponseDto = GenericMessageResponseDto
