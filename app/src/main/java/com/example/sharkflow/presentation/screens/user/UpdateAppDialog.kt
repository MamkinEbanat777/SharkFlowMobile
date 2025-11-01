package com.example.sharkflow.presentation.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sharkflow.domain.model.UpdateInfo

@Composable
fun UpdateAvailableDialog(
    updateInfo: UpdateInfo,
    onDismiss: () -> Unit,
    onDownload: suspend () -> Unit
) {
    var isDownloading by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Доступно обновление ${updateInfo.versionName}") },
        text = {
            Column {
                updateInfo.notes?.let { Text(it) }
                Spacer(Modifier.height(8.dp))
                Text("Размер: приблизительно (скачивается при клике)")
            }
        },
        confirmButton = {
            TextButton(onClick = {
            }) {
                if (isDownloading) Text("Скачивание...") else Text("Скачать и установить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Позже") }
        }
    )
}
