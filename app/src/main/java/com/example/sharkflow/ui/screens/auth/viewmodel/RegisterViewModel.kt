package com.example.sharkflow.ui.screens.auth.viewmodel

import androidx.lifecycle.*
import com.example.sharkflow.data.repository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun register(
        login: String,
        email: String,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = registerRepository.register(login, email, password, confirmPassword)
                result.onSuccess { onSuccess() }
                result.onFailure { _errorMessage.value = it.message }
            } catch (e: Exception) {
                _errorMessage.value = "Произошла ошибка. Проверьте подключение."
            } finally {
                _isLoading.value = false
            }
        }
    }
}


