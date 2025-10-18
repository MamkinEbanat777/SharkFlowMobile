package com.example.sharkflow.data.repository

import com.example.sharkflow.BuildConfig
import com.example.sharkflow.data.manager.CloudinaryManager
import com.example.sharkflow.domain.repository.CloudinaryRepository
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.*

@Singleton
class CloudinaryRepositoryImpl @Inject constructor(
    private val manager: CloudinaryManager
) : CloudinaryRepository {
    override suspend fun uploadImage(
        imageBytes: ByteArray,
        accessToken: String?,
        csrfToken: String?
    ): Result<Pair<String, String>> = withContext(Dispatchers.IO) {
        manager.setUploading(true)
        try {
            val sigResponse = OkHttpClient().newCall(
                Request.Builder()
                    .url("${BuildConfig.BASE_URL}cloudinary-signature")
                    .header("Authorization", "Bearer $accessToken")
                    .apply { csrfToken?.let { header("X-CSRF-TOKEN", it) } }
                    .get()
                    .build()
            ).execute()

            if (!sigResponse.isSuccessful) {
                return@withContext Result.failure(Exception("Ошибка при получении подписи"))
            }

            val sigJson = JSONObject(sigResponse.body.string())
            val apiKey = sigJson.optString("api_key", "")
            val signature = sigJson.optString("signature", "")
            val timestamp = sigJson.optLong("timestamp", -1)

            if (apiKey.isBlank() || signature.isBlank() || timestamp == -1L) {
                return@withContext Result.failure(Exception("Некорректные параметры подписи"))
            }

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "file", "file",
                    imageBytes.toRequestBody("image/*".toMediaType(), 0, imageBytes.size)
                )
                .addFormDataPart("api_key", apiKey)
                .addFormDataPart("timestamp", timestamp.toString())
                .addFormDataPart("upload_preset", "Precet-SharkFlow")
                .addFormDataPart("signature", signature)
                .build()

            val request = Request.Builder()
                .url("https://api.cloudinary.com/v1_1/dyilzwof1/image/upload")
                .post(requestBody)
                .build()

            val response = OkHttpClient().newCall(request).execute()

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("Ошибка при загрузке изображения: ${response.message}"))
            }

            val json = JSONObject(response.body.string())
            val uploadedUrl = json.getString("secure_url")
            val uploadedPublicId = json.getString("public_id")

            val resultPair = Pair(uploadedUrl, uploadedPublicId)
            manager.setLastUpload(resultPair)

            Result.success(resultPair)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            manager.setUploading(false)
        }
    }
}
