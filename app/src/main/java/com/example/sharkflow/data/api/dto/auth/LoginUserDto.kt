package com.example.sharkflow.data.api.dto.auth

import com.google.gson.annotations.SerializedName

data class LoginUserDto(
    @SerializedName("user") val user: LoginRequestDto
)