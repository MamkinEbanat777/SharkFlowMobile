package com.example.sharkflow.ui.components

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
            onValueChange = { login = it },
            label = Lang.string(R.string.register_label_login)
        )

        AppField(
            value = email,
            onValueChange = { email = it },
            label = Lang.string(R.string.register_label_email)
        )

        AppField(
            value = password,
            onValueChange = { password = it },
            label = Lang.string(R.string.register_label_password), isPassword = true,
            showPassword = showPassword,
            onToggleVisibility = { showPassword = !showPassword }
        )

        AppField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = Lang.string(R.string.register_label_confirm_password),
            isPassword = true,
            showPassword = showPassword
        )

        if (registerViewModel.errorMessage != null) {
            Text(registerViewModel.errorMessage!!, color = colorScheme.error)
        }

        AppButton(
            onClick = {
                val anyEmpty =
                    login.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
                if (anyEmpty) {
                    showEmptyFieldsWarning = true
                } else {
                    showEmptyFieldsWarning = false
                    registerViewModel.register(login, email, password, confirmPassword) {
                        onNext()
                    }
                }
            },
            variant = AppButtonVariant.Primary,
            text = if (registerViewModel.isLoading) Lang.string(R.string.register_button_sending)
            else Lang.string(R.string.register_button_register),
            enabled = !registerViewModel.isLoading,
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