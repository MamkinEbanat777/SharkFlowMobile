package com.example.sharkflow.ui.screens.profile.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.example.sharkflow.BuildConfig
import com.example.sharkflow.data.repository.*
import com.example.sharkflow.utils.AppLog
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val currentUser = userRepository.currentUser
    var isLoading by mutableStateOf(true)
        private set

    var avatarPublicId: String? = null

    init {
        viewModelScope.launch {
            tokenRepository.hasToken.collect { hasToken ->
                if (hasToken) {
                    loadUser()
                } else {
                    userRepository.clearUser()
                    isLoading = false
                }
            }
        }
    }

    fun loadUser() {
        viewModelScope.launch {
            isLoading = true
            userRepository.loadUser()
            isLoading = false
        }
    }

    fun requestDeleteUserCode(onResult: (success: Boolean, message: String?) -> Unit) {
        viewModelScope.launch {
            val result = userRepository.requestDeleteUserCode()
            result.fold(
                onSuccess = { message -> onResult(true, message) },
                onFailure = { error -> onResult(false, error.message) }
            )
        }
    }

    fun confirmDeleteUser(
        code: String,
        onResult: (success: Boolean, message: String?) -> Unit
    ) {
        viewModelScope.launch {
            val result = userRepository.confirmDeleteUser(code)
            result.fold(
                onSuccess = { message ->
                    tokenRepository.clearTokens()
                    userRepository.clearUser()
                    onResult(true, message)
                },
                onFailure = { error -> onResult(false, error.message) }
            )
        }
    }

    fun requestUpdateUserCode(onResult: (success: Boolean, message: String?) -> Unit) {
        viewModelScope.launch {
            val result = userRepository.requestUpdateUserCode()
            result.fold(
                onSuccess = { message -> onResult(true, message) },
                onFailure = { e -> onResult(false, e.message) }
            )
        }
    }

    fun confirmUpdateUser(
        code: String,
        email: String,
        login: String,
        onResult: (success: Boolean, message: String?) -> Unit
    ) {
        viewModelScope.launch {
            val result = userRepository.confirmUpdateUser(code, email, login)
            result.fold(
                onSuccess = { message -> onResult(true, message) },
                onFailure = { error -> onResult(false, error.message) }
            )
        }
    }

    fun updateUserAvatar(
        avatarUrl: String,
        publicId: String,
        onResult: (success: Boolean, message: String?) -> Unit
    ) {
        viewModelScope.launch {
            val result = userRepository.updateUserAvatar(avatarUrl, publicId)
            result.fold(
                onSuccess = { message -> onResult(true, message) },
                onFailure = { error -> onResult(false, error.message) }
            )
        }
    }

    fun deleteUserAvatar(onResult: (success: Boolean, message: String?) -> Unit) {
        viewModelScope.launch {
            val result = userRepository.deleteUserAvatar()
            result.fold(
                onSuccess = { message -> onResult(true, message) },
                onFailure = { error -> onResult(false, error.message) }
            )
        }
    }

    suspend fun uploadToCloudinary(context: Context, uri: Uri): Pair<String, String>? =
        withContext(Dispatchers.IO) {
            try {
                val (accessToken, csrfToken) = tokenRepository.loadTokens()

                val sigRequest = Request.Builder()
                    .url("${BuildConfig.BASE_URL}cloudinary-signature")
                    .header("Authorization", "Bearer $accessToken")
                    .apply {
                        csrfToken?.let { header("X-CSRF-TOKEN", it) }
                    }
                    .get()
                    .build()

                val sigResponse = OkHttpClient().newCall(sigRequest).execute()

                if (!sigResponse.isSuccessful) {
                    AppLog.e(
                        "CloudinaryUpload",
                        "Ошибка при получении подписи: ${sigResponse.message}"
                    )
                    return@withContext null
                }

                val sigJson = JSONObject(sigResponse.body.string())

                val apiKey = sigJson.optString("api_key", "")
                val signature = sigJson.optString("signature", "")
                val timestamp = sigJson.optLong("timestamp", -1)

                if (apiKey == null || signature == null || timestamp == -1L) {
                    AppLog.e(
                        "CloudinaryUpload",
                        "Ошибка: Не удалось получить все параметры для подписи"
                    )
                    return@withContext null
                }

                val inputStream = context.contentResolver.openInputStream(uri)
                if (inputStream == null) {
                    AppLog.e("CloudinaryUpload", "Ошибка: не удалось открыть поток для файла")
                    return@withContext null
                }

                val content = inputStream.readBytes()
                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "file",
                        "file",
                        content.toRequestBody("image/*".toMediaType(), 0, content.size)
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

                if (response.isSuccessful) {
                    val json = JSONObject(response.body.string())
                    val uploadedUrl = json.getString("secure_url")
                    val uploadedPublicId = json.getString("public_id")
                    return@withContext Pair(uploadedUrl, uploadedPublicId)
                } else {
                    AppLog.e(
                        "CloudinaryUpload",
                        "Ошибка при загрузке изображения на Cloudinary: ${response.message}"
                    )
                    return@withContext null
                }

            } catch (e: Exception) {
                AppLog.e("CloudinaryUpload", "Исключение при загрузке изображения: ${e.message}")
                return@withContext null
            }
        }

}
