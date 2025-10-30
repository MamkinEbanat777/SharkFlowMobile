package com.example.sharkflow.presentation.common

import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable

@Composable
fun ConfirmationModal(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable (() -> Unit)? = null,
    confirmButtonText: String,
    dismissButtonText: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colorScheme.background,
        title = { Text(title, style = MaterialTheme.typography.titleMedium) },
        text = {
            if (content != null) {
                content()
            } else {
                Text(message, style = MaterialTheme.typography.bodyMedium)
            }
        },
        confirmButton = {
            AppButton(onClick = {
                onConfirm()
                onDismiss()
            }, text = confirmButtonText, variant = AppButtonVariant.Text)
        },
        dismissButton = {
            AppButton(onClick = {
                onDismiss()
            }, text = dismissButtonText, variant = AppButtonVariant.Text)
        }
    )
}