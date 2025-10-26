package com.example.sharkflow.presentation.screens.auth.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sharkflow.R
import com.example.sharkflow.presentation.common.*
import com.example.sharkflow.presentation.screens.auth.viewmodel.LoginViewModel
import com.example.sharkflow.utils.*

@Composable
fun LoginForm(navController: NavController) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val emptyFieldWarning = Lang.string(R.string.login_empty_fields_warning)

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    var emailError by rememberSaveable { mutableStateOf(false) }
    var passwordError by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    val isLoading by loginViewModel.isLoading.collectAsState()
    val errorMessage by loginViewModel.errorMessage.collectAsState()
    val successMessage by loginViewModel.successMessage.collectAsState()

    ObserveToastMessages(
        errorMessage = errorMessage,
        successMessage = successMessage
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 2.dp, color = colorScheme.primary, shape = RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = Lang.string(R.string.login_title),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 20.dp)
        )

        AppField(
            value = email,
            onValueChange = {
                email = it
                if (emailError) emailError = false
            },
            label = Lang.string(R.string.login_email_label),
            isError = emailError
        )

        AppField(
            value = password,
            onValueChange = {
                password = it
                if (passwordError) passwordError = false
            },
            label = Lang.string(R.string.login_password_label),
            isPassword = true,
            showPassword = passwordVisible,
            onToggleVisibility = { passwordVisible = !passwordVisible },
            isError = passwordError
        )

        errorMessage?.let {
            Text(it, color = colorScheme.error)
        }

        AppButton(
            onClick = {
                var hasError = false
                if (email.isEmpty()) {
                    emailError = true
                    hasError = true
                }
                if (password.isEmpty()) {
                    passwordError = true
                    hasError = true
                }

                if (hasError) {
                    ToastManager.warning(context, emptyFieldWarning)
                } else {
                    loginViewModel.login(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            text = if (isLoading)
                Lang.string(R.string.login_button_sending)
            else
                Lang.string(R.string.login_button_send),
            enabled = !isLoading
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(Lang.string(R.string.login_no_account))
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(onClick = { navController.navigate("register") }) {
                Text(Lang.string(R.string.login_create_account), color = colorScheme.primary)
            }
        }
    }
}
