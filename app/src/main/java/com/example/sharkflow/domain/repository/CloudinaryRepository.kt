package com.example.sharkflow.domain.repository

interface CloudinaryRepository {
    suspend fun uploadImage(
        imageBytes: ByteArray, // Вместо Uri передаем ByteArray
        accessToken: String?,
        csrfToken: String?
    ): Result<Pair<String, String>>
}
