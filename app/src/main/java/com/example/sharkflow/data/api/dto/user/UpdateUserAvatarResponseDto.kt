package com.example.sharkflow.data.api.dto.user

import com.google.gson.annotations.SerializedName

data class UpdateUserAvatarResponseDto(
    @SerializedName("message")
    val message: String,
    @SerializedName("avatarUrl")
    val avatarUrl: String
)