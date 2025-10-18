package com.example.sharkflow.ui.screens.auth.viewmodel

import androidx.lifecycle.*
import com.example.sharkflow.domain.repository.RegisterRepository
import com.example.sharkflow.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmationCodeViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    fun confirmationCode(
        code: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = registerRepository.confirmCode(code)

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    _errorMessage.value = ErrorMapper.map(response.code(), errorBodyString)
                }

            } catch (e: Exception) {
                AppLog.e("Network exception", e)
                _errorMessage.value = "Сервер недоступен, пожалуйста повторите попытку позже"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
