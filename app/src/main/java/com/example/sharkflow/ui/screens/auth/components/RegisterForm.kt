package com.example.sharkflow.ui.screens.auth.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sharkflow.R
import com.example.sharkflow.data.local.language.Lang
import com.example.sharkflow.ui.common.*
import com.example.sharkflow.ui.screens.auth.viewmodel.RegisterViewModel
import com.example.sharkflow.utils.ToastManager

@Composable
fun RegisterForm(
    onNext: () -> Unit,
    navController: NavController
) {
    val registerViewModel: RegisterViewModel = hiltViewModel()
    val context = LocalContext.current

    val emptyWarning = Lang.string(R.string.register_warning_empty_fields)

    var login by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showPassword by remember { mutableStateOf(false) }
    var showEmptyFieldsWarning by remember { mutableStateOf(false) }

    var loginError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }

    val isLoading by registerViewModel.isLoading.collectAsState()
    val errorMessage by registerViewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 2.dp, color = colorScheme.primary, shape = RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = Lang.string(R.string.register_title),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 20.dp)
        )

        AppField(
            value = login,
            onValueChange = {
                login = it
                if (loginError) loginError = false
            },
            label = Lang.string(R.string.register_label_login),
            isError = loginError
        )

        AppField(
            value = email,
            onValueChange = {
                email = it
                if (emailError) emailError = false
            },
            label = Lang.string(R.string.register_label_email),
            isError = emailError
        )

        AppField(
            value = password,
            onValueChange = {
                password = it
                if (passwordError) passwordError = false
            },
            label = Lang.string(R.string.register_label_password),
            isPassword = true,
            showPassword = showPassword,
            onToggleVisibility = { showPassword = !showPassword },
            isError = passwordError
        )

        AppField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                if (confirmPasswordError) confirmPasswordError = false
            },
            label = Lang.string(R.string.register_label_confirm_password),
            isPassword = true,
            showPassword = showPassword,
            isError = confirmPasswordError
        )

        if (errorMessage != null) {
            errorMessage?.let { Text(it, color = colorScheme.error) }
        }

        AppButton(
            onClick = {
                var hasError = false
                if (login.isEmpty()) {
                    loginError = true; hasError = true
                }
                if (email.isEmpty()) {
                    emailError = true; hasError = true
                }
                if (password.isEmpty()) {
                    passwordError = true; hasError = true
                }
                if (confirmPassword.isEmpty()) {
                    confirmPasswordError = true; hasError = true
                }

                if (hasError) {
                    ToastManager.warning(context, emptyWarning)
                } else {
                    registerViewModel.register(login, email, password, confirmPassword) {
                        onNext()
                    }
                }
            },
            variant = AppButtonVariant.Primary,
            text = if (isLoading) Lang.string(R.string.register_button_sending)
            else Lang.string(R.string.register_button_register),
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        if (showEmptyFieldsWarning) {
            LaunchedEffect(Unit) {
                ToastManager.warning(context, emptyWarning)
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(Lang.string(R.string.register_text_have_account))
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(onClick = { navController.navigate("login") }) {
                Text(Lang.string(R.string.register_text_login), color = colorScheme.primary)
            }
        }
    }
}