package com.example.sharkflow.ui.screens.auth.viewmodel

import androidx.lifecycle.*
import com.example.sharkflow.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

@HiltViewModel
class AuthStateViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {
        viewModelScope.launch {
            tokenRepository.hasToken.collect { has ->
                _isLoggedIn.value = has
            }
        }
    }

    fun refreshToken(baseClient: OkHttpClient, baseUrl: String, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val success = authRepository.refreshToken(baseClient, baseUrl)
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