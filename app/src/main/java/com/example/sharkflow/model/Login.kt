package com.example.sharkflow.model

import com.google.gson.annotations.*

data class LoginRequest(
    val email: String,
    val password: String,
    val rememberMe: Boolean = true,
)

data class LoginUser(
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


