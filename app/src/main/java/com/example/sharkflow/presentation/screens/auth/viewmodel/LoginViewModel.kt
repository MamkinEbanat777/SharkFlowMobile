package com.example.sharkflow.presentation.screens.auth.viewmodel

import androidx.lifecycle.*
import com.example.sharkflow.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
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
                val loginResult = authRepository.login(email, password)
                if (loginResult.isSuccess) {
                    val userResult = userRepository.loadUser()
                    if (userResult.isSuccess) {
                        val user = userResult.getOrNull()!!
                        _successMessage.value = "Добро пожаловать! ${user.login}"
                    } else {
                        _errorMessage.value = userResult.exceptionOrNull()?.message
                    }

                } else {
                    _errorMessage.value =
                        loginResult.exceptionOrNull()?.message ?: "Ошибка авторизации"
                }
            } finally {
                _isLoading.value = false
            }
        }

    }


}