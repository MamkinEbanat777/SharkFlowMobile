package com.example.sharkflow.presentation.screens.auth.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.sharkflow.R
import com.example.sharkflow.core.common.Lang
import com.example.sharkflow.core.presentation.ToastManager
import com.example.sharkflow.core.validators.RegisterValidator
import com.example.sharkflow.presentation.common.*
import com.example.sharkflow.presentation.screens.auth.viewmodel.ConfirmationCodeViewModel

@Composable
fun CodeConfirmation(
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val confirmationCodeViewModel: ConfirmationCodeViewModel = hiltViewModel()

    var confirmationCode by remember { mutableStateOf("") }
    var confirmationCodeError by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val animatedCharColor by animateColorAsState(
        targetValue = if (confirmationCodeError) colorScheme.error else colorScheme.primary,
        label = "otp_char_color"
    )

    val isLoading by confirmationCodeViewModel.isLoading.collectAsState()
    val errorMessage by confirmationCodeViewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        ToastManager.info(
            context,
            "На вашу почту отправлен код подтверждения",
            true
        )
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = Lang.string(R.string.code_confirmation_title),
            style = typography.titleMedium,
            color = colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        OtpView(
            otpText = confirmationCode,
            onOtpTextChange = { code ->
                confirmationCode = code
                if (confirmationCodeError && code.isNotEmpty()) {
                    confirmationCodeError =
                        false
                }
            },
            otpCount = 6,
            containerSize = 40.dp,
            charSize = 28.sp,
            type = OTP_VIEW_TYPE_UNDERLINE,
            charColor = animatedCharColor,
            charBackground = colorScheme.surfaceVariant.copy(alpha = 0.1f),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(4.dp))

        errorMessage?.let {
            Text(it, color = colorScheme.error)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            AppButton(
                onBack,
                text = Lang.string(R.string.code_confirmation_back),
                variant = AppButtonVariant.Outlined
            )
            AppButton(
                onClick = {
                    val codeValid =
                        RegisterValidator.validateConfirmationCode(confirmationCode, context)
                    if (!codeValid) {
                        confirmationCodeError = true
                    } else {
                        confirmationCodeError = false
                        confirmationCodeViewModel.confirmationCode(confirmationCode) {
                            onNext()
                        }
                    }
                },
                text = if (isLoading)
                    Lang.string(R.string.code_confirmation_checking)
                else
                    Lang.string(R.string.code_confirmation_send),
                enabled = !isLoading
            )
        }
    }
}
