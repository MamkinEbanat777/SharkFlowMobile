package com.example.sharkflow.ui.screens.auth.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.example.sharkflow.data.repository.AuthRepository
import com.example.sharkflow.model.UserResponse
import com.example.sharkflow.utils.AppLog
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) :
    ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var successMessage by mutableStateOf<String?>(null)
        private set

    var currentUser by mutableStateOf<UserResponse?>(null)
        private set

    fun login(
        email: String,
        password: String,
        onSuccess: (UserResponse) -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            try {
                val result = authRepository.login(email, password)
                if (result.isSuccess) {
                    val user = result.getOrNull()!!
                    currentUser = user
                    onSuccess(user)
                    successMessage = "Добро пожаловать! ${user.login}"
                } else {
                    errorMessage = result.exceptionOrNull()?.message ?: "Неизвестная ошибка"
                }
            } catch (e: Exception) {
                AppLog.e("Ошибка авторизации", e)
                errorMessage = "Произошла ошибка. Проверьте подключение."
            } finally {
                isLoading = false
            }
        }
    }
}