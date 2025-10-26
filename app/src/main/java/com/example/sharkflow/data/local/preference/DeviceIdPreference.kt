package com.example.sharkflow.data.local.preference

import android.content.Context
import androidx.core.content.edit
import java.util.UUID
import javax.inject.Inject

class DeviceIdPreference @Inject constructor(context: Context) {
    private val prefs =
        context.applicationContext.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun getOrCreateDeviceId(): String {
        var id = prefs.getString(KEY_DEVICE_ID, null)
        if (id.isNullOrEmpty()) {
            id = UUID.randomUUID().toString()
            prefs.edit { putString(KEY_DEVICE_ID, id) }
        }
        return id
    }

    companion object {
        private const val KEY_DEVICE_ID = "device_id"
    }
}
