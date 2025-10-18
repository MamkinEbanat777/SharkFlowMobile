package com.example.sharkflow.data.manager

import com.example.sharkflow.domain.model.User
import jakarta.inject.*
import kotlinx.coroutines.flow.*

@Singleton
class UserManager @Inject constructor() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun setUser(user: User?) {
        _currentUser.value = user
    }

    fun clearUser() {
        _currentUser.value = null
    }
}
