package com.example.sharkflow.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

open class BaseViewModel :
    ViewModel() {
    protected val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    protected val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    protected val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    protected fun <T> launchResult(
        block: suspend () -> Result<T>,
        onSuccess: (T) -> Unit = { result ->
            _successMessage.value = "$result"
        },
        onFailure: (Throwable?) -> Unit = { throwable ->
            _errorMessage.value = throwable?.message
        }
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _successMessage.value = null
            try {
                val result = block()
                result.fold(
                    onSuccess = onSuccess,
                    onFailure = { throwable -> onFailure(throwable) }
                )
            } catch (e: Exception) {
                onFailure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
