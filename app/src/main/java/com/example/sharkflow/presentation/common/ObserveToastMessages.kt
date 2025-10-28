package com.example.sharkflow.presentation.common

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.sharkflow.core.presentation.ToastManager

@Composable
fun ObserveToastMessages(
    errorMessage: String?,
    successMessage: String?,
) {
    val context = LocalContext.current

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            ToastManager.error(context, it)
        }
    }

    LaunchedEffect(successMessage) {
        successMessage?.let {
            ToastManager.success(context, it)
        }
    }
}
