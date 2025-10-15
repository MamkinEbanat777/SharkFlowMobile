package com.example.sharkflow.model

import com.google.gson.annotations.SerializedName


data class ConfirmationCodeRequest(
    @SerializedName("confirmationCode")
    val confirmationCode: String
)


data class ConfirmationCodeResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("error")
    val error: String?
)


