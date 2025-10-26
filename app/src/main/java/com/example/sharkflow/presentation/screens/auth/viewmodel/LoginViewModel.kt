package com.example.sharkflow.presentation.screens.auth.viewmodel

import com.example.sharkflow.domain.usecase.auth.LoginUseCase
import com.example.sharkflow.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {
    fun login(email: String, password: String) {
        launchResult(
            block = { loginUseCase(email, password) },
            onSuccess = { message ->
                _successMessage.value = message
            },
            onFailure = { throwable ->
                _errorMessage.value = throwable?.message ?: "Ошибка авторизации"
            }
        )
    }
}
