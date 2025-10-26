package com.example.sharkflow.presentation.screens.auth.viewmodel

import com.example.sharkflow.domain.usecase.auth.ConfirmRegistrationCodeUseCase
import com.example.sharkflow.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ConfirmationCodeViewModel @Inject constructor(
    private val confirmRegistrationCodeUseCase: ConfirmRegistrationCodeUseCase
) : BaseViewModel() {

    private val _isConfirmed = MutableStateFlow(false)
    val isConfirmed: StateFlow<Boolean> = _isConfirmed

    fun confirmationCode(
        code: String,
        onSuccess: () -> Unit
    ) {
        launchResult(
            block = { confirmRegistrationCodeUseCase(code) }, onSuccess = {
                _isConfirmed.value = true
                onSuccess()
            },
            onFailure = { throwable ->
                _errorMessage.value =
                    throwable?.message ?: "Сервер недоступен, пожалуйста повторите попытку позже"
            })
    }
}
