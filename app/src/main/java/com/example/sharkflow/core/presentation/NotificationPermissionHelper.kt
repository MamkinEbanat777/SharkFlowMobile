package com.example.sharkflow.core.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

fun ComponentActivity.requestNotificationPermissionIfNeeded(onResult: (granted: Boolean) -> Unit = {}) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        onResult(true)
        return
    }

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
        == PackageManager.PERMISSION_GRANTED
    ) {
        onResult(true)
        return
    }

    val launcher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            onResult(granted)
        }

    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
}
