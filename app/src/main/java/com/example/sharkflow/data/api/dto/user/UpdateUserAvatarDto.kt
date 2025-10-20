package com.example.sharkflow.data.api.dto.user

import com.google.gson.annotations.SerializedName

data class UpdateUserAvatarRequestDto(
    @SerializedName("imgUrl")
    val imgUrl: String,
    @SerializedName("publicId")
    val publicId: String? = null
)

data class UpdateUserAvatarResponseDto(
    val message: String,
    val avatarUrl: String
)
