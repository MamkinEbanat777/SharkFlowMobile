package com.example.sharkflow.ui.screens.auth.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.sharkflow.BuildConfig
import com.example.sharkflow.data.network.AuthManager
import com.example.sharkflow.data.repository.*
import com.example.sharkflow.utils.AppLog
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

@HiltViewModel
class AuthStateViewModel @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val tokenRepository: TokenRepository,
    private val authRepository: AuthRepository,
    private val authManager: AuthManager,
) : ViewModel() {
    var isLoggedIn = mutableStateOf(false)

    init {
        viewModelScope.launch {
            tokenRepository.hasToken.collect { has ->
                isLoggedIn.value = has
            }
        }
    }

    fun refreshToken(onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val success = try {
                authManager.refreshToken(okHttpClient, BuildConfig.BASE_URL)
            } catch (e: Exception) {
                AppLog.e("Token refresh failed", e)
                false
            }
            onResult(success)
        }
    }

    fun logout(onResult: (success: Boolean, message: String?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            val result = authRepository.logout()
            result.fold(
                onSuccess = { message ->
                    onResult(true, message)
                },
                onFailure = { error ->
                    onResult(false, error.message)
                }
            )
        }
    }
}