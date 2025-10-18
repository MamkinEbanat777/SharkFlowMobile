package com.example.sharkflow.domain.model

import com.google.gson.annotations.SerializedName


data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("rememberMe")
    val rememberMe: Boolean = true,
)


data class LoginUser(
    @SerializedName("user")
    val user: LoginRequest
)


data class LoginResponse(
    @SerializedName("accessToken")
    val accessToken: String? = null,

    @SerializedName("csrfToken")
    val csrfToken: String? = null,

    @SerializedName("deviceId")
    val deviceId: String? = null,

    @SerializedName("githubOAuthEnabled")
    val githubOAuthEnabled: Boolean? = null,

    @SerializedName("error")
    val error: String? = null
)


