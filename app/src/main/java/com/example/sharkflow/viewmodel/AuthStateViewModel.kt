package com.example.sharkflow.viewmodel

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.example.sharkflow.BuildConfig
import com.example.sharkflow.data.api.UserApi
import com.example.sharkflow.data.network.AuthManager
import com.example.sharkflow.data.repository.TokenRepository
import com.example.sharkflow.model.UserResponse
import com.example.sharkflow.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Response

@HiltViewModel
class AuthStateViewModel @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val tokenRepository: TokenRepository,
    private val userApi: UserApi,
) : ViewModel() {
    var currentUser by mutableStateOf<UserResponse?>(null)
    var isLoggedIn by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    fun initialLoad(context: Context) {
        viewModelScope.launch {
            isLoading = true
            try {
                val (accessToken, _) = tokenRepository.loadTokens()
                val hasToken = !accessToken.isNullOrBlank()

                if (!hasToken) {
                    isLoggedIn = false
                    return@launch
                }

                if (refreshToken(context)) {
                    getUserData()
                } else {
                    clearUserData()

                }
            } catch (e: Exception) {
                AppLog.e("Error during initial load", e)
                clearUserData()
            } finally {
                isLoading = false
            }
        }
    }

    private suspend fun refreshToken(context: Context): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                AuthManager.refreshToken(
                    context = context,
                    baseClient = okHttpClient,
                    baseUrl = BuildConfig.BASE_URL
                )
            }
        } catch (e: Exception) {
            AppLog.e("Token refresh failed", e)
            false
        }
    }

    private suspend fun getUserData() {
        try {
            val response: Response<UserResponse> = withContext(Dispatchers.IO) {
                userApi.getUser()
            }

            if (response.isSuccessful) {
                currentUser = response.body()
                isLoggedIn = true
                AppLog.d("AuthVM", "User loaded successfully: $currentUser")
            } else {
                clearUserData()
                AppLog.w("AuthVM", "Failed to load user data: ${response.code()}")
            }
        } catch (e: Exception) {
            AppLog.e("Failed to get user data", e)
            clearUserData()
        }
    }

    private fun clearUserData() {
        tokenRepository.clearTokens()
        isLoggedIn = false
        currentUser = null
        AppLog.d("AuthVM", "User data cleared")
    }

    fun setUser(user: UserResponse?) {
        currentUser = user
        isLoggedIn = user != null
    }

    fun logout(
        onResult: (success: Boolean, message: String?) -> Unit = { _, _ -> }
    ) {
        viewModelScope.launch {
            var success = false
            var message: String? = null
            try {
                val response = userApi.logoutUser()
                if (response.isSuccessful) {
                    success = true
                    message = response.body()?.message
                    tokenRepository.clearTokens()
                    clearUserData()
                } else {
                    message = ErrorMapper.map(response.code(), response.errorBody()?.string())
                    AppLog.d("AuthVM", "Logout failed: $message")
                }
            } catch (e: Exception) {
                AppLog.e("Logout failed: ${e.message}", e)
                message = "Ошибка сети, попробуйте снова."
            } finally {
                onResult(success, message)
            }
        }
    }
}

