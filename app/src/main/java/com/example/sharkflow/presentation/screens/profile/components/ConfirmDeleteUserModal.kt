package com.example.sharkflow.presentation.screens.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sharkflow.R
import com.example.sharkflow.core.common.Lang
import com.example.sharkflow.presentation.common.*
import com.example.sharkflow.presentation.screens.profile.viewmodel.UserProfileViewModel

@Composable
fun ConfirmDeleteUserModal(
    onDismiss: () -> Unit,
    userProfileViewModel: UserProfileViewModel,
    onSuccess: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Подтверждение удаления аккаунта",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column {
                Text(
                    "Вы уверены, что хотите удалить аккаунт? Внимание! Это действие необратимо!",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Ошибка: $errorMessage",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            AppButton(
                variant = AppButtonVariant.Text,
                onClick = {
                    isLoading = true
                    errorMessage = null
                    userProfileViewModel.requestDeleteUserCode { success, message ->
                        isLoading = false
                        if (success) {
                            onSuccess()
                        } else {
                            errorMessage = message ?: "Ошибка при отправке кода"
                        }
                    }
                },
                text = Lang.string(R.string.common_yes)
            )
        },
        dismissButton = {
            AppButton(
                variant = AppButtonVariant.Text,
                onClick = onDismiss,
                text = Lang.string(R.string.common_no)
            )
        }
    )
}
