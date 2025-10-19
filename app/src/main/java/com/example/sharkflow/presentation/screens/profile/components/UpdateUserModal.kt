package com.example.sharkflow.presentation.screens.profile.components


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sharkflow.presentation.common.AppField
import com.example.sharkflow.presentation.screens.profile.viewmodel.UserProfileViewModel

@Composable
fun UpdateUserModal(
    onDismiss: () -> Unit,
    userProfileViewModel: UserProfileViewModel,
    onSuccess: () -> Unit
) {
    var step by remember { mutableIntStateOf(1) }
    var code by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(userProfileViewModel.currentUser.value?.email ?: "") }
    var login by remember { mutableStateOf(userProfileViewModel.currentUser.value?.login ?: "") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = {
            Text(
                text = if (step == 1) "Подтвердите обновление данных" else "Введите код и новые данные",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column {
                if (step == 1) {
                    Text(
                        "Вы хотите обновить данные аккаунта. Продолжить?",
                        style = MaterialTheme.typography.bodyMedium
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
                } else {
                    Text(
                        "На вашу почту отправлен код. Введите его и новые данные:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    AppField(
                        value = code,
                        onValueChange = { code = it },
                        label = "Код с почты",
                        enabled = !isLoading,
                        isError = errorMessage != null,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AppField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        enabled = !isLoading,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AppField(
                        value = login,
                        onValueChange = { login = it },
                        label = "Логин",
                        enabled = !isLoading,
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
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (step == 1) {
                        isLoading = true
                        errorMessage = null
                        userProfileViewModel.requestUpdateUserCode { success, message ->
                            isLoading = false
                            if (success) {
                                step = 2
                            } else {
                                errorMessage = message ?: "Ошибка при отправке кода"
                            }
                        }
                    } else {
                        if (code.isBlank() || email.isBlank() || login.isBlank()) {
                            errorMessage = "Все поля должны быть заполнены"
                            return@TextButton
                        }
                        isLoading = true
                        errorMessage = null
                        userProfileViewModel.confirmUpdateUser(
                            code,
                            email,
                            login
                        ) { success, message ->
                            isLoading = false
                            if (success) onSuccess() else errorMessage =
                                message ?: "Ошибка обновления"
                        }
                    }
                },
                enabled = !isLoading
            ) {
                Text(if (step == 1) "Продолжить" else "Обновить")
            }
        },

        dismissButton = {
            TextButton(onClick = { if (!isLoading) onDismiss() }, enabled = !isLoading) {
                Text("Отмена")
            }
        }
    )
}

