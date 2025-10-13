package com.example.sharkflow.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import com.example.sharkflow.utils.*

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
