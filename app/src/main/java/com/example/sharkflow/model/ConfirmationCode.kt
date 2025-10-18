package com.example.sharkflow.model

import com.google.gson.annotations.SerializedName


data class ConfirmationCodeRequest(
    @SerializedName("confirmationCode")
    val confirmationCode: String
)

data class ConfirmationCodeResponse(
    override val message: String?,
    override val error: String?
) : BaseResponse(message, error)


