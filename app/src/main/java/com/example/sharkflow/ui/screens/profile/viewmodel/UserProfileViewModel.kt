package com.example.sharkflow.ui.screens.profile.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import com.example.sharkflow.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository,
    private val cloudinaryRepository: CloudinaryRepository
) : ViewModel() {
    val currentUser = userRepository.currentUser
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _avatarPublicId = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            tokenRepository.hasToken.collect { hasToken ->
                if (hasToken) {
                    loadUser()
                } else {
                    userRepository.clearUser()
                    _isLoading.value = false
                }
            }
        }
    }

    fun loadUser() {
        viewModelScope.launch {
            _isLoading.value = true
            userRepository.loadUser()
            _isLoading.value = false
        }
    }

    fun requestDeleteUserCode(onResult: (success: Boolean, message: String?) -> Unit) {
        viewModelScope.launch {
            val result = userRepository.requestDeleteUserCode()
            result.fold(
                onSuccess = { message -> onResult(true, message) },
                onFailure = { error -> onResult(false, error.message) }
            )
        }
    }

    fun confirmDeleteUser(
        code: String,
        onResult: (success: Boolean, message: String?) -> Unit
    ) {
        viewModelScope.launch {
            val result = userRepository.confirmDeleteUser(code)
            result.fold(
                onSuccess = { message ->
                    tokenRepository.clearTokens()
                    onResult(true, message)
                },
                onFailure = { error -> onResult(false, error.message) }
            )
        }
    }

    fun requestUpdateUserCode(onResult: (success: Boolean, message: String?) -> Unit) {
        viewModelScope.launch {
            val result = userRepository.requestUpdateUserCode()
            result.fold(
                onSuccess = { message -> onResult(true, message) },
                onFailure = { e -> onResult(false, e.message) }
            )
        }
    }

    fun confirmUpdateUser(
        code: String,
        email: String,
        login: String,
        onResult: (success: Boolean, message: String?) -> Unit
    ) {
        viewModelScope.launch {
            val result = userRepository.confirmUpdateUser(code, email, login)
            result.fold(
                onSuccess = { message -> onResult(true, message) },
                onFailure = { error -> onResult(false, error.message) }
            )
        }
    }

    fun updateUserAvatar(
        avatarUrl: String,
        publicId: String,
        onResult: (success: Boolean, message: String?) -> Unit
    ) {
        viewModelScope.launch {
            val result = userRepository.updateUserAvatar(avatarUrl, publicId)
            result.fold(
                onSuccess = { message -> onResult(true, message) },
                onFailure = { error -> onResult(false, error.message) }
            )
        }
    }

    fun deleteUserAvatar(onResult: (success: Boolean, message: String?) -> Unit) {
        viewModelScope.launch {
            val result = userRepository.deleteUserAvatar()
            result.fold(
                onSuccess = { message -> onResult(true, message) },
                onFailure = { error -> onResult(false, error.message) }
            )
        }
    }

    fun uploadUserAvatar(
        context: Context,
        uri: Uri,
        onResult: (success: Boolean, url: String?, publicId: String?) -> Unit
    ) {
        viewModelScope.launch {
            val (accessToken, csrfToken) = tokenRepository.loadTokens()
            val result = cloudinaryRepository.uploadImage(context, uri, accessToken, csrfToken)
            result.fold(
                onSuccess = { (url, publicId) -> onResult(true, url, publicId) },
                onFailure = { onResult(false, null, null) }
            )
        }
    }

    fun setAvatarPublicId(publicId: String?) {
        _avatarPublicId.value = publicId
    }

}
