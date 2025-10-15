package com.example.sharkflow.model

import com.google.gson.annotations.SerializedName


data class Refresh(
    @SerializedName("accessToken")
    val accessToken: String?,
    @SerializedName("csrfToken")
    val csrfToken: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("error")
    val error: String?,
)