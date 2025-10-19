package com.example.sharkflow.presentation.common

import androidx.compose.material3.*
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
        title = { Text(title, style = MaterialTheme.typography.titleMedium) },
        text = {
            if (content != null) {
                content()
            } else {
                Text(message, style = MaterialTheme.typography.bodyMedium)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(dismissButtonText)
            }
        }
    )
}