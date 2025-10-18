package com.example.sharkflow.data.service

import com.example.sharkflow.domain.model.UserResponse
import jakarta.inject.*
import kotlinx.coroutines.flow.*

@Singleton
class UserManager @Inject constructor() {
    private val _currentUser = MutableStateFlow<UserResponse?>(null)
    val currentUser: StateFlow<UserResponse?> = _currentUser

    fun setUser(user: UserResponse?) {
        _currentUser.value = user
    }

    fun clearUser() {
        _currentUser.value = null
    }
}
