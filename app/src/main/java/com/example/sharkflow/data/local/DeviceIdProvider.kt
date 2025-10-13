package com.example.sharkflow.data.local

import android.content.*
import androidx.core.content.*
import java.util.*
import javax.inject.*

class DeviceIdProvider @Inject constructor(context: Context) {
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
