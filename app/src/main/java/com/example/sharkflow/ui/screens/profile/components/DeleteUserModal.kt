package com.example.sharkflow.ui.screens.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sharkflow.R
import com.example.sharkflow.data.local.language.Lang
import com.example.sharkflow.ui.screens.profile.viewmodel.UserProfileViewModel

@Composable
fun DeleteUserModal(
    onDismiss: () -> Unit,
    userProfileViewModel: UserProfileViewModel,
    onSuccess: () -> Unit
) {
    var code by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("Введите код подтверждения", style = MaterialTheme.typography.titleMedium) },
        text = {
            Column {
                Text(
                    "На вашу почту отправлен код для удаления аккаунта. Введите его ниже:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Код с почты") },
                    singleLine = true,
                    isError = errorMessage != null,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
                if (isLoading) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (code.isBlank()) {
                        errorMessage = "Код не может быть пустым"
                        return@TextButton
                    }
                    isLoading = true
                    errorMessage = null
                    userProfileViewModel.confirmDeleteUser(code) { success, message ->
                        isLoading = false
                        if (success) onSuccess() else errorMessage =
                            message ?: "Ошибка при удалении"
                    }
                },
                enabled = !isLoading
            ) {
                Text("Удалить")
            }
        },
        dismissButton = {
            TextButton(onClick = { if (!isLoading) onDismiss() }, enabled = !isLoading) {
                Text(Lang.string(R.string.common_no))
            }
        }
    )
}
