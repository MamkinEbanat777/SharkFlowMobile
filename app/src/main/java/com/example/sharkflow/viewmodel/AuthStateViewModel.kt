package com.example.sharkflow.viewmodel

import android.content.*
import android.util.*
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.example.sharkflow.*
import com.example.sharkflow.data.api.*
import com.example.sharkflow.data.network.*
import com.example.sharkflow.data.repository.*
import com.example.sharkflow.model.*
import com.example.sharkflow.utils.*
import dagger.hilt.android.lifecycle.*
import jakarta.inject.*
import kotlinx.coroutines.*
import okhttp3.*
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
                Log.e("AuthVM", "Error during initial load", e)
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
            Log.e("AuthVM", "Token refresh failed", e)
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
                Log.d("AuthVM", "User loaded successfully: $currentUser")
            } else {
                clearUserData()
                Log.w("AuthVM", "Failed to load user data: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("AuthVM", "Failed to get user data", e)
            clearUserData()
        }
    }

    private fun clearUserData() {
        tokenRepository.clearTokens()
        isLoggedIn = false
        currentUser = null
        Log.d("AuthVM", "User data cleared")
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
                    isLoggedIn = false
                    currentUser = null
                } else {
                    message = ErrorMapper.map(response.code(), response.errorBody()?.string())
                    Log.d("AuthVM", "Logout failed: $message")
                }
            } catch (e: Exception) {
                Log.e("AuthStateVM", "Logout failed: ${e.message}", e)
                message = "Ошибка сети, попробуйте снова."
            } finally {
                onResult(success, message)
            }
        }
    }
}

