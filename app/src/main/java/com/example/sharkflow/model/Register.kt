package com.example.sharkflow.model

import com.google.gson.annotations.SerializedName


data class RegisterRequest(
    @SerializedName("login")
    val login: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("confirmPassword")
    val confirmPassword: String,
    @SerializedName("acceptedPolicies")
    val acceptedPolicies: Boolean = true,
)


data class RegisterUser(
    @SerializedName("user")
    val user: RegisterRequest
)


data class RegisterResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("error")
    val error: String?
)