package com.example.sharkflow.ui.screens.auth.viewmodel

import android.util.*
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.example.sharkflow.data.repository.*
import com.example.sharkflow.model.*
import dagger.hilt.android.lifecycle.*
import jakarta.inject.*
import kotlinx.coroutines.*

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
                    onSuccess(user)
                    successMessage = "Добро пожаловать! ${currentUser?.login}"
                } else {
                    errorMessage = result.exceptionOrNull()?.message ?: "Неизвестная ошибка"
                }
            } catch (e: Exception) {
                Log.e("LoginVM", "Ошибка авторизации", e)
                errorMessage = "Произошла ошибка. Проверьте подключение."
            } finally {
                isLoading = false
            }
        }
    }
}