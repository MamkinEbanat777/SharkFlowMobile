package com.example.sharkflow.ui.screens.auth.viewmodel

import android.util.*
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.example.sharkflow.data.repository.*
import com.example.sharkflow.utils.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import javax.inject.*

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
                Log.d("ConfirmCodeVM", "Response: ${response.body()}")

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    Log.d("LoginVM", "Error body: $errorBodyString")
                    errorMessage = ErrorMapper.map(response.code(), errorBodyString)
                }

            } catch (e: Exception) {
                Log.e("ConfirmationCodeVM", "Network exception", e)
                errorMessage = "Сервер недоступен, пожалуйста повторите попытку позже"
            } finally {
                isLoading = false
            }
        }
    }
}
