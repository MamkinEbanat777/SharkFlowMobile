package com.example.sharkflow.ui.screens.auth.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.sharkflow.R
import com.example.sharkflow.data.local.language.Lang
import com.example.sharkflow.ui.common.*
import com.example.sharkflow.ui.screens.auth.viewmodel.ConfirmationCodeViewModel
import com.example.sharkflow.utils.ToastManager

@Composable
fun CodeConfirmation(
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val confirmationCodeViewModel: ConfirmationCodeViewModel = hiltViewModel()

    var confirmationCode by remember { mutableStateOf("") }
    var confirmationCodeError by remember { mutableStateOf(false) }

    var showEmptyFieldWarning by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val warningMessage = Lang.string(R.string.code_confirmation_warning)

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = Lang.string(R.string.code_confirmation_title),
            style = typography.titleMedium,
            color = colorScheme.onBackground
        )

        AppField(
            value = confirmationCode,
            onValueChange = {
                confirmationCode = it
                if (confirmationCodeError) confirmationCodeError = false
            },
            label = Lang.string(R.string.code_confirmation_label),
            isError = confirmationCodeError
        )

        confirmationCodeViewModel.errorMessage?.let {
            Text(it, color = colorScheme.error)
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
                Text(Lang.string(R.string.code_confirmation_back))
            }

            AppButton(
                onClick = {
                    if (confirmationCode.isEmpty()) {
                        confirmationCodeError = true
                        ToastManager.warning(context, warningMessage)
                    } else {
                        confirmationCodeError = false
                        confirmationCodeViewModel.confirmationCode(confirmationCode) {
                            onNext()
                        }
                    }
                },
                variant = AppButtonVariant.Primary,
                text = if (confirmationCodeViewModel.isLoading)
                    Lang.string(R.string.code_confirmation_checking)
                else
                    Lang.string(R.string.code_confirmation_send),
                enabled = !confirmationCodeViewModel.isLoading
            )

            if (showEmptyFieldWarning) {
                LaunchedEffect(Unit) {
                    ToastManager.warning(context, warningMessage)
                }
            }

        }
    }
}
