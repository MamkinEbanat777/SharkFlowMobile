// время сервера не совпадает с cloudinary поэтому пока так

package com.example.sharkflow.data.repository.remote

import com.example.sharkflow.core.system.AppLog
import com.example.sharkflow.domain.repository.CloudinaryRepository
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.*

//
//@Singleton
//class CloudinaryRepositoryImpl @Inject constructor(
//) : CloudinaryRepository {
//    override suspend fun uploadImage(
//        imageBytes: ByteArray,
//        accessToken: String?,
//        csrfToken: String?
//    ): Result<Pair<String, String>> = withContext(Dispatchers.IO) {
//        try {
//            val sigResponse = OkHttpClient().newCall(
//                Request.Builder()
//                    .url("${BuildConfig.BASE_URL}cloudinary-signature")
//                    .header("Authorization", "Bearer $accessToken")
//                    .apply { csrfToken?.let { header("X-CSRF-TOKEN", it) } }
//                    .get()
//                    .build()
//            ).execute()
//
//            val sigBodyString = sigResponse.body?.string() ?: ""
//            println("CloudinarySignatureResponse: successful=${sigResponse.isSuccessful}, body=$sigBodyString")
//
//            if (!sigResponse.isSuccessful) {
//                return@withContext Result.failure(Exception("Ошибка при получении подписи"))
//            }
//
//            val sigJson = JSONObject(sigBodyString)
//            val apiKey = sigJson.optString("api_key", "")
//            val signature = sigJson.optString("signature", "")
//            val timestamp = sigJson.optLong("timestamp", -1)
//
//            if (apiKey.isBlank() || signature.isBlank() || timestamp == -1L) {
//                return@withContext Result.failure(Exception("Некорректные параметры подписи"))
//            }
//
//            val requestBody = MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart(
//                    "file", "file",
//                    imageBytes.toRequestBody("image/*".toMediaType(), 0, imageBytes.size)
//                )
//                .addFormDataPart("api_key", apiKey)
//                .addFormDataPart("timestamp", timestamp.toString())
//                .addFormDataPart("upload_preset", "Precet-SharkFlow")
//                .addFormDataPart("signature", signature)
//                .build()
//
//            val request = Request.Builder()
//                .url("https://api.cloudinary.com/v1_1/dyilzwof1/image/upload")
//                .post(requestBody)
//                .build()
//
//            val response = OkHttpClient().newCall(request).execute()
//
//            val responseBodyString = response.body?.string() ?: ""
//            println("CloudinaryUploadResponse: successful=${response.isSuccessful}, body=$responseBodyString")
//
//            if (!response.isSuccessful) {
//                return@withContext Result.failure(Exception("Ошибка при загрузке изображения: ${response.message}"))
//            }
//
//            val json = JSONObject(responseBodyString)
//            val uploadedUrl = json.getString("secure_url")
//            val uploadedPublicId = json.getString("public_id")
//
//            val resultPair = Pair(uploadedUrl, uploadedPublicId)
//
//            Result.success(resultPair)
//        } catch (e: Exception) {
//            println("CloudinaryUploadException: ${e.message}")
//            e.printStackTrace()
//            Result.failure(e)
//        }
//    }
//}


@Singleton
class CloudinaryRepositoryImpl @Inject constructor() : CloudinaryRepository {
    override suspend fun uploadImage(
        imageBytes: ByteArray,
        accessToken: String?,
        csrfToken: String?
    ): Result<Pair<String, String>> = withContext(Dispatchers.IO) {
        try {
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "file", "file",
                    imageBytes.toRequestBody("image/*".toMediaType(), 0, imageBytes.size)
                )
                .addFormDataPart("upload_preset", "Precet-SharkFlow")
                .build()

            val request = Request.Builder()
                .url("https://api.cloudinary.com/v1_1/dyilzwof1/image/upload")
                .post(requestBody)
                .build()

            val response = OkHttpClient().newCall(request).execute()
            val responseBodyString = response.body?.string() ?: ""
            println("CloudinaryUploadResponse: successful=${response.isSuccessful}, body=$responseBodyString")

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("Ошибка при загрузке изображения: ${response.message}"))
            }

            val json = JSONObject(responseBodyString)
            val uploadedUrl = json.getString("secure_url")
            val uploadedPublicId = json.getString("public_id")
            AppLog.i("uploadedPublicId: $uploadedPublicId")

            Result.success(Pair(uploadedUrl, uploadedPublicId))
        } catch (e: Exception) {
            println("CloudinaryUploadException: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
