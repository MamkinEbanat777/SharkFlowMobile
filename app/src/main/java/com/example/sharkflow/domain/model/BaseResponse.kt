package com.example.sharkflow.domain.model

import com.google.gson.annotations.SerializedName

open class BaseResponse(
    @SerializedName("message")
    open val message: String? = null,
    @SerializedName("error")
    open val error: String? = null
)
