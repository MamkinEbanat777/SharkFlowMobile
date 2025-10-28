package com.example.sharkflow.data.api.dto.auth

import com.google.gson.annotations.SerializedName

data class RegisterRequestDto(
    @SerializedName("login") val login: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("confirmPassword") val confirmPassword: String,
    @SerializedName("acceptedPolicies") val acceptedPolicies: Boolean = true
)

