package com.example.sharkflow.data.service

import jakarta.inject.*
import kotlinx.coroutines.flow.*

@Singleton
class RegisterService @Inject constructor() {
    private val _isRegistered = MutableStateFlow(false)
    val isRegistered: StateFlow<Boolean> = _isRegistered.asStateFlow()

    fun setRegistered(value: Boolean) {
        _isRegistered.value = value
    }
}
