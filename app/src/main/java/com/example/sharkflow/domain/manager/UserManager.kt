package com.example.sharkflow.domain.manager

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

    fun updateAvatar(url: String, publicId: String) {
        _currentUser.value?.let { user ->
            setUser(user.copy(avatarUrl = url, publicId = publicId))
        }
    }

    fun clearUser() {
        _currentUser.value = null
    }

    fun clearAvatar() {
        _currentUser.value?.let { user ->
            setUser(user.copy(avatarUrl = "", publicId = ""))
        }
    }

}