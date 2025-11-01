package com.example.sharkflow.domain.model

data class UpdateInfo(
    val versionCode: Int,
    val versionName: String,
    val notes: String?,
    val apkUrl: String,
    val sha256: String?,
    val minSdk: Int? = null
)
