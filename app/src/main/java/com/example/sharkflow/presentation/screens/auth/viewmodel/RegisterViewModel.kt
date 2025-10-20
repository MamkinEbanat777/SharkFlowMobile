package com.example.sharkflow.presentation.screens.auth.viewmodel

import com.example.sharkflow.domain.usecase.user.auth.RegisterUserUseCase
import com.example.sharkflow.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : BaseViewModel() {
    private val _isRegistered = MutableStateFlow(false)
    val isRegistered: StateFlow<Boolean> = _isRegistered

    fun register(
        login: String,
        email: String,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit
    ) {
        launchResult(
            block = { registerUserUseCase(login, email, password, confirmPassword) },
            onSuccess = {
                _isRegistered.value = true
                onSuccess()
            },
            onFailure = { throwable ->
                _errorMessage.value =
                    throwable?.message ?: "Произошла ошибка. Проверьте подключение."
            }
        )
    }
}


