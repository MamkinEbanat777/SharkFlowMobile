package com.example.sharkflow.presentation.screens.auth.viewmodel

import androidx.lifecycle.*
import com.example.sharkflow.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase

) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _successMessage.value = null
            try {
                val result = loginUseCase(email, password)
                if (result.isSuccess) {
                    _successMessage.value = result.getOrNull()
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "Ошибка авторизации"
                }
            } finally {
                _isLoading.value = false
            }
        }

    }
}