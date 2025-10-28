package com.example.sharkflow.data.api.dto.task

import com.google.gson.annotations.SerializedName

data class DeletedTaskInfoDto(
    @SerializedName("title") val title: String,
    @SerializedName("removedFromBoard") val removedFromBoard: Boolean = true
)