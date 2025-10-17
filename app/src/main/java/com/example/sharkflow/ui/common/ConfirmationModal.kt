package com.example.sharkflow.ui.common

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.example.sharkflow.R
import com.example.sharkflow.data.local.language.Lang

@Composable
fun ConfirmationModal(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable (() -> Unit)? = null,
    confirmButtonText: String = Lang.string(R.string.common_yes),
    dismissButtonText: String = Lang.string(R.string.common_no)
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