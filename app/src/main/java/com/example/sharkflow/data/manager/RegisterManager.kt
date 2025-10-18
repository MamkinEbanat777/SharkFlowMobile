package com.example.sharkflow.data.manager

import jakarta.inject.*
import kotlinx.coroutines.flow.*

@Singleton
class RegisterManager @Inject constructor() {
    private val _isRegistered = MutableStateFlow(false)
    val isRegistered: StateFlow<Boolean> = _isRegistered.asStateFlow()

    fun setRegistered(value: Boolean) {
        _isRegistered.value = value
    }
}
