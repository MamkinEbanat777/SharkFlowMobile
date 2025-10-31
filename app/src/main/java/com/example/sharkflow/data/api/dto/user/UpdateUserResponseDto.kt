package com.example.sharkflow.data.api.dto.user

import com.example.sharkflow.domain.model.User
import com.google.gson.annotations.SerializedName

data class UpdateUserResponseDto(
    @SerializedName("message")
    val message: String,
    @SerializedName("user")
    val user: User?
)

