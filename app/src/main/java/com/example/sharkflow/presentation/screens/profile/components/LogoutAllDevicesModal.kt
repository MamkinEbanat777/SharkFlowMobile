package com.example.sharkflow.presentation.screens.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.sharkflow.presentation.common.*
import com.example.sharkflow.presentation.screens.auth.viewmodel.AuthStateViewModel

@Composable
fun LogoutAllDevicesModal(
    onDismiss: () -> Unit,
    authStateViewModel: AuthStateViewModel,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text("Вы уверены что хотите выйти со всех устройств?", style = typography.titleMedium)
        },
        text = {
            Column {
                Text(
                    text = "Примечание: После выхода другие устройства ещё будут работать примерно 15 минут. ",
                    style = typography.bodySmall
                )
            }
        },
        confirmButton = {
            AppButton(
                onClick = { authStateViewModel.logoutFromAllDevices() },
                text = "Да, выйти из всех",
                icon = Icons.AutoMirrored.Filled.Logout,
                modifier = Modifier.fillMaxWidth(),
                tone = AppButtonTone.Danger,
                variant = AppButtonVariant.Outlined
            )
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) { Text("Закрыть") }
        }
    )

}