package com.example.sharkflow.model

import com.google.gson.annotations.SerializedName


data class UserResponse(
    @SerializedName("login")
    val login: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("role")
    val role: String?,
    @SerializedName("avatarUrl")
    val avatarUrl: String?
)


data class LogoutResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("error")
    val error: String?
)