package com.example.sharkflow.ui.screens.auth.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.example.sharkflow.data.repository.RegisterRepository
import com.example.sharkflow.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmationCodeViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun confirmationCode(
        code: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = registerRepository.confirmCode(code)

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    errorMessage = ErrorMapper.map(response.code(), errorBodyString)
                }

            } catch (e: Exception) {
                AppLog.e("Network exception", e)
                errorMessage = "Сервер недоступен, пожалуйста повторите попытку позже"
            } finally {
                isLoading = false
            }
        }
    }
}
