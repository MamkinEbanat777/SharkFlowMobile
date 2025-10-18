package com.example.sharkflow.data.manager

import jakarta.inject.*
import kotlinx.coroutines.flow.*

@Singleton
class CloudinaryManager @Inject constructor() {
    private val _lastUpload = MutableStateFlow<Pair<String, String>?>(null)
    val lastUpload: StateFlow<Pair<String, String>?> = _lastUpload.asStateFlow()

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    fun setUploading(uploading: Boolean) {
        _isUploading.value = uploading
    }

    fun setLastUpload(pair: Pair<String, String>?) {
        _lastUpload.value = pair
    }

    fun clearLastUpload() {
        _lastUpload.value = null
    }
}
