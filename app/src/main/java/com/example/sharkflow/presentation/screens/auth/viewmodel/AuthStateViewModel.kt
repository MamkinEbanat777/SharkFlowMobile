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
    private val logoutUserUseCase: LogoutUserUseCase,
    private val logoutFromAllDevicesUseCase: LogoutFromAllDevicesUseCase,
    private val logoutFromDeviceUseCase: LogoutFromDeviceUseCase,
    private val clearTokensUseCase: ClearTokensUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase
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

    fun refreshToken(onResult: (Boolean) -> Unit = {}) {
        launchResult(
            block = { Result.success(refreshTokenUseCase()) },
            onSuccess = { success -> onResult(success) },
            onFailure = { onResult(false) }
        )
    }


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

    fun logoutFromAllDevices(onResult: (success: Boolean, message: String?) -> Unit = { _, _ -> }) {
        launchResult(
            block = { logoutFromAllDevicesUseCase() },
            onSuccess = { responseDto ->
                clearTokensUseCase()
                onResult(true, (responseDto as? Any)?.toString())
            },
            onFailure = { throwable ->
                onResult(false, throwable?.message)
            }
        )
    }

    fun logoutFromDevice(deviceId: String, onResult: (success: Boolean) -> Unit = {}) {
        launchResult(
            block = { logoutFromDeviceUseCase(deviceId) },
            onSuccess = { _ ->
                onResult(true)
            },
            onFailure = { _ ->
                onResult(false)
            }
        )
    }

}