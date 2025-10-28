package com.example.sharkflow.presentation.screens.task.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.example.sharkflow.presentation.common.*

@Composable
fun ConfirmDeleteDialog(
    title: String = "Delete task?",
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            AppButton(
                variant = AppButtonVariant.Text,
                onClick = { onConfirm(); onDismiss() },
                text = "Удалить"
            )
        },
        dismissButton = {
            AppButton(variant = AppButtonVariant.Text, onClick = onDismiss, text = "Отмена")
        }
    )
}
