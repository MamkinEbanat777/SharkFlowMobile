package com.example.sharkflow.data.api.dto.auth

import com.google.gson.annotations.SerializedName

data class RegisterUserDto(
    @SerializedName("user") val user: RegisterRequestDto
)