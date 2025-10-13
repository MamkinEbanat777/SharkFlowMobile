package com.example.sharkflow.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*
import androidx.hilt.lifecycle.viewmodel.compose.*
import com.example.sharkflow.ui.screens.auth.viewmodel.*
import com.example.sharkflow.utils.*

@Composable
fun CodeConfirmation(
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val confirmationCodeViewModel: ConfirmationCodeViewModel = hiltViewModel()

    var confirmationCode by remember { mutableStateOf("") }
    var showEmptyFieldWarning by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Введите код подтверждения",
            style = typography.titleMedium,
            color = colorScheme.onBackground
        )

        AppField(
            value = confirmationCode,
            onValueChange = { confirmationCode = it },
            label = "Код подтверждения",
        )

        if (confirmationCodeViewModel.errorMessage != null) {
            Text(confirmationCodeViewModel.errorMessage!!, color = colorScheme.error)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp, 16.dp, 16.dp, 8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colorScheme.primary
                )

            ) {
                Text("Назад")
            }

            AppButton(
                onClick = {
                    confirmationCode.isEmpty()
                    if (confirmationCode.isEmpty()) {
                        showEmptyFieldWarning = true
                    } else {
                        showEmptyFieldWarning = false
                        confirmationCodeViewModel.confirmationCode(confirmationCode) {
                            onNext()
                        }
                    }
                },
                variant = AppButtonVariant.Primary,
                text = (if (confirmationCodeViewModel.isLoading) "Проверка..." else "Отправить"),
                enabled = !confirmationCodeViewModel.isLoading
            )

            if (showEmptyFieldWarning) {
                ToastManager.warning(context, "Пожалуйста, введите код подтверждения")
                /*Text(
                    text = "Пожалуйста, введите код подтверждения",
                    color = colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                ) */
            }
        }
    }
}
