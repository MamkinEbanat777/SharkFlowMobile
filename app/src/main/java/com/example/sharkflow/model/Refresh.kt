package com.example.sharkflow.model

import com.google.gson.annotations.SerializedName


data class Refresh(
    @SerializedName("accessToken")
    val accessToken: String?,
    @SerializedName("csrfToken")
    val csrfToken: String?,
    override val message: String?,
    override val error: String?,
) : BaseResponse(message, error)