package com.example.sharkflow.data.api.dto.user

import com.google.gson.annotations.SerializedName

data class UpdateAvatarRequestDto(
    @SerializedName("imgUrl")
    val imgUrl: String,
    @SerializedName("publicId")
    val publicId: String? = null
)