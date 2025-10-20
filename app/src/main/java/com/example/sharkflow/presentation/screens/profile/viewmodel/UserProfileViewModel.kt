package com.example.sharkflow.presentation.screens.profile.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.sharkflow.domain.manager.UserManager
import com.example.sharkflow.domain.usecase.user.delete.*
import com.example.sharkflow.domain.usecase.user.init.InitializeUserSessionUseCase
import com.example.sharkflow.domain.usecase.user.update.*
import com.example.sharkflow.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val initializeUserSessionUseCase: InitializeUserSessionUseCase,
    private val userManager: UserManager,
    private val uploadUserAvatarUseCase: UploadUserAvatarUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val requestUpdateUserCodeUseCase: RequestUpdateUserCodeUseCase,
    private val requestDeleteUserCodeUseCase: RequestDeleteUserCodeUseCase,
    private val deleteUserAvatarUseCase: DeleteUserAvatarUseCase,
    private val updateUserAvatarUseCase: UpdateUserAvatarUseCase,
    private val deleteUserAccountUseCase: DeleteUserAccountUseCase
) : BaseViewModel() {
    val currentUser = userManager.currentUser

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    private val _isUserLoading = MutableStateFlow(false)
    val isUserLoading: StateFlow<Boolean> = _isUserLoading.asStateFlow()

    private val _lastUpload = MutableStateFlow<Pair<String, String>?>(null)
    val lastUpload: StateFlow<Pair<String, String>?> = _lastUpload.asStateFlow()

    val avatarUrl = userManager.currentUser.map { it?.avatarUrl }
    val avatarPublicId = userManager.currentUser.map { it?.publicId }

    init {
        viewModelScope.launch {
            _isUserLoading.value = true
            initializeUserSessionUseCase()
            _isUserLoading.value = false
        }
    }

    fun requestDeleteUserCode(onResult: (success: Boolean, message: String?) -> Unit) {
        launchResult(
            block = { requestDeleteUserCodeUseCase() },
            onSuccess = { message -> onResult(true, message) },
            onFailure = { throwable -> onResult(false, throwable?.message) }
        )
    }

    fun confirmDeleteUser(code: String, onResult: (Boolean, String?) -> Unit) {
        launchResult(
            block = { deleteUserAccountUseCase(code) },
            onSuccess = { message -> onResult(true, message) },
            onFailure = { throwable -> onResult(false, throwable?.message) }
        )
    }

    fun requestUpdateUserCode(onResult: (Boolean, String?) -> Unit) {
        launchResult(
            block = { requestUpdateUserCodeUseCase() },
            onSuccess = { message -> onResult(true, message) },
            onFailure = { throwable -> onResult(false, throwable?.message) }
        )
    }

    fun confirmUpdateUser(
        code: String,
        email: String,
        login: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        launchResult(
            block = { updateUserUseCase(code, email, login) },
            onSuccess = { message -> onResult(true, message) },
            onFailure = { throwable -> onResult(false, throwable?.message) }
        )
    }

    fun updateUserAvatar(
        avatarUrl: String,
        publicId: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        launchResult(
            block = { updateUserAvatarUseCase(avatarUrl, publicId) },
            onSuccess = { response ->
                userManager.updateAvatar(avatarUrl, publicId)
                onResult(true, response.message)
            },
            onFailure = { throwable -> onResult(false, throwable?.message) }
        )
    }

    fun deleteUserAvatar(onResult: (Boolean, String?) -> Unit) {
        launchResult(
            block = { deleteUserAvatarUseCase() },
            onSuccess = { message -> onResult(true, message) },
            onFailure = { throwable -> onResult(false, throwable?.message) }
        )
    }

    fun uploadUserAvatar(
        imageBytes: ByteArray,
        onResult: (Boolean, String?, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isUploading.value = true
            try {
                val result = uploadUserAvatarUseCase(imageBytes)
                result.fold(
                    onSuccess = { (url, publicId) ->
                        _lastUpload.value = url to publicId
                        updateUserAvatar(url, publicId) { success, message ->
                            onResult(success, url, publicId)
                        }
                    },
                    onFailure = {
                        onResult(false, null, null)
                    }
                )
            } finally {
                _isUploading.value = false
            }
        }
    }

}
