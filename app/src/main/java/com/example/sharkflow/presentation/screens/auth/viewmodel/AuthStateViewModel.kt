package com.example.sharkflow.presentation.screens.auth.viewmodel

import androidx.lifecycle.*
import com.example.sharkflow.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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

    fun refreshToken(onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val success = authRepository.refreshToken()
            onResult(success)
        }
    }

    fun logout(onResult: (success: Boolean, message: String?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            val result = authRepository.logout()
            result.fold(
                onSuccess = { message ->
                    _isLoggedIn.value = false
                    onResult(true, message)
                },
                onFailure = { error ->
                    onResult(false, error.message ?: "Неизвестная ошибка")
                }
            )
        }
    }

}