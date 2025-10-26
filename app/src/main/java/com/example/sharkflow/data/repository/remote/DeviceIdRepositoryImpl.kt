package com.example.sharkflow.data.repository.remote

import android.content.Context
import androidx.core.content.edit
import com.example.sharkflow.domain.repository.DeviceIdRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import java.util.UUID
import javax.inject.Singleton

@Singleton
class DeviceIdRepositoryImpl @Inject constructor(
    private val context: Context
) : DeviceIdRepository {

    private val prefs by lazy { context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }
    private val KEY_DEVICE_ID = "device_id"

    private val _deviceId: MutableStateFlow<String> = MutableStateFlow(readOrCreate())

    override fun deviceIdFlow(): StateFlow<String> = _deviceId

    override fun getOrCreateDeviceId(): String = _deviceId.value

    private fun readOrCreate(): String {
        val existing = prefs.getString(KEY_DEVICE_ID, null)
        return if (!existing.isNullOrEmpty()) {
            existing
        } else {
            val id = UUID.randomUUID().toString()
            prefs.edit { putString(KEY_DEVICE_ID, id) }
            id
        }
    }
}
