package com.example.sharkflow.presentation.screens.user.viewmodel

import com.example.sharkflow.core.common.UaParser
import com.example.sharkflow.core.system.AppLog
import com.example.sharkflow.domain.manager.UserManager
import com.example.sharkflow.domain.model.UserSession
import com.example.sharkflow.domain.usecase.auth.DeviceIdUseCase
import com.example.sharkflow.domain.usecase.user.delete.*
import com.example.sharkflow.domain.usecase.user.get.*
import com.example.sharkflow.domain.usecase.user.init.InitializeUserSessionUseCase
import com.example.sharkflow.domain.usecase.user.update.*
import com.example.sharkflow.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*

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
    private val deleteUserAccountUseCase: DeleteUserAccountUseCase,
    private val loadUserSessionsUseCase: LoadUserSessionsUseCase,
    private val loadUserUseCase: LoadUserUseCase,
    private val deviceIdUseCase: DeviceIdUseCase
) : BaseViewModel() {
    val currentUser = userManager.currentUser
    private var hasLoadedUser = false

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    private val _isUserLoading = MutableStateFlow(false)
    val isUserLoading: StateFlow<Boolean> = _isUserLoading.asStateFlow()

    private val _lastUpload = MutableStateFlow<Pair<String, String>?>(null)
    val lastUpload: StateFlow<Pair<String, String>?> = _lastUpload.asStateFlow()

    val avatarUrl = userManager.currentUser.map { it?.avatarUrl }
    val avatarPublicId = userManager.currentUser.map { it?.publicId }

    private val _sessions = MutableStateFlow<List<UserSession>>(emptyList())
    val sessions: StateFlow<List<UserSession>> = _sessions.asStateFlow()

    private val _isLoadingSessions = MutableStateFlow(false)
    val isLoadingSessions: StateFlow<Boolean> = _isLoadingSessions.asStateFlow()

    private val _sessionsError = MutableStateFlow<String?>(null)
    val sessionsError: StateFlow<String?> = _sessionsError.asStateFlow()

    val currentDeviceId: StateFlow<String> by lazy { deviceIdUseCase() }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    fun loadUser() {
        if (hasLoadedUser) return
        hasLoadedUser = true
        launchResult(
            block = { initializeUserSessionUseCase(); Result.success(Unit) },
            onSuccess = { _isUserLoading.value = false },
            onFailure = { throwable ->
                _isUserLoading.value = false
                AppLog.e("UserProfileViewModel", "loadUser failed", throwable)
            }
        )
    }

    fun refreshUser(onResult: ((success: Boolean, error: String?) -> Unit)? = null) {
        launchResult(
            block = { loadUserUseCase() },
            onSuccess = { user ->
                userManager.setUser(user)
                onResult?.invoke(true, null)
            },
            onFailure = { throwable ->
                AppLog.e("UserProfileViewModel", "refreshUser failed", throwable)
                onResult?.invoke(false, throwable?.message ?: "Ошибка")
            }
        )
    }

    fun loadSessions() {
        launchResult(
            block = { loadUserSessionsUseCase() },
            onSuccess = { list ->
                val enriched = list.map { s ->
                    val parsed = UaParser.parse(s.userAgent)
                    s.copy(
                        osName = s.osName ?: parsed.os,
                        osVersion = s.osVersion ?: parsed.osVersion,
                        deviceBrand = s.deviceBrand ?: parsed.deviceBrand,
                        deviceModel = s.deviceModel ?: parsed.deviceModel,
                        clientName = s.clientName ?: parsed.browser,
                        clientVersion = s.clientVersion ?: parsed.browserVersion
                    )
                }
                _sessions.value = enriched
            },
            onFailure = { throwable ->
                _sessionsError.value = throwable?.message ?: "Ошибка загрузки сессий"
            }
        )
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
        _isUploading.value = true
        launchResult(
            block = { uploadUserAvatarUseCase(imageBytes) },
            onSuccess = { (url, publicId) ->
                _lastUpload.value = url to publicId
                updateUserAvatar(url, publicId) { success, _ ->
                    onResult(success, url, publicId)
                }
                _isUploading.value = false
            },
            onFailure = {
                onResult(false, null, null)
                _isUploading.value = false
            }
        )

    }

    fun markSessionInactive(deviceId: String) {
        _sessions.value = _sessions.value.map { session ->
            if (session.deviceId == deviceId) session.copy(isActive = false) else session
        }
    }
}
