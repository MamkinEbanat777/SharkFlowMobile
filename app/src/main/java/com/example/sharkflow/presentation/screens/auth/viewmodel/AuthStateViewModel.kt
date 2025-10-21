package com.example.sharkflow.presentation.screens.auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.sharkflow.domain.usecase.auth.*
import com.example.sharkflow.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class AuthStateViewModel @Inject constructor(
    private val checkTokenUseCase: CheckTokenUseCase,
    private val logoutUserUseCase: LogoutUserUseCase
) : BaseViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {
        viewModelScope.launch {
            checkTokenUseCase().collect { hasToken ->
                _isLoggedIn.value = hasToken
            }
        }
    }

//    fun refreshToken(onResult: (Boolean) -> Unit = {}) {
//        viewModelScope.launch {
//            val success = authRepository.refreshToken()
//            onResult(success)
//        }
//    }

    fun logout(onResult: (success: Boolean, message: String?) -> Unit = { _, _ -> }) {
        launchResult(
            block = { logoutUserUseCase() },
            onSuccess = { message ->
                _isLoggedIn.value = false
                onResult(true, message)
            },
            onFailure = { throwable ->
                onResult(
                    false,
                    throwable?.message ?: "Неизвестная ошибка"
                )
            })
    }
}