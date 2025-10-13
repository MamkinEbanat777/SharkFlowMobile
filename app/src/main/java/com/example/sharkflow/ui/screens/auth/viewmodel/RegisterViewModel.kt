package com.example.sharkflow.ui.screens.auth.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.example.sharkflow.data.repository.*
import dagger.hilt.android.lifecycle.*
import jakarta.inject.*
import kotlinx.coroutines.*

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun register(
        login: String,
        email: String,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = registerRepository.register(login, email, password, confirmPassword)
            result.onSuccess { onSuccess() }
            result.onFailure { errorMessage = it.message }
            isLoading = false
        }
    }
}


