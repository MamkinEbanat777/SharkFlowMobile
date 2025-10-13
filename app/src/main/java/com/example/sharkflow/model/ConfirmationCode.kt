package com.example.sharkflow.model

data class ConfirmationCodeRequest(
    val confirmationCode: String
)

data class ConfirmationCodeResponse(
    val message: String?,
    val error: String?
)


