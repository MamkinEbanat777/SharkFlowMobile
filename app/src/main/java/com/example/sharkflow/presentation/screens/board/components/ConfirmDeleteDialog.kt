package com.example.sharkflow.presentation.screens.board.components

import android.os.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.sharkflow.presentation.common.*

@Composable
fun ConfirmDeleteDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val vibrator = context.getSystemService(Vibrator::class.java)

    var inputText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colorScheme.background,
        title = { Text(title) },
        text = {
            Column {
                Text(message)
                Spacer(modifier = Modifier.height(12.dp))
                AppField(inputText, { inputText = it }, "Введите 'Удалить'")
            }
        },
        confirmButton = {
            AppButton(
                onClick = {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            80,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )

                    onConfirm()
                },
                enabled = inputText.lowercase().trim() == "удалить",
                text = "Удалить",
                variant = AppButtonVariant.Text
            )
        },
        dismissButton = {
            AppButton(onClick = onDismiss, text = "Отмена", variant = AppButtonVariant.Text)

        }
    )
}
