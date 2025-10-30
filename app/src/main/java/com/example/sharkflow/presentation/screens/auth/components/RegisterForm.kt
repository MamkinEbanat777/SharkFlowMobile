package com.example.sharkflow.presentation.screens.auth.components

import androidx.compose.foundation.*
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
import com.example.sharkflow.core.common.Lang
import com.example.sharkflow.core.validators.RegisterValidator
import com.example.sharkflow.presentation.common.*
import com.example.sharkflow.presentation.screens.auth.viewmodel.RegisterViewModel

@Composable
fun RegisterForm(
    onNext: () -> Unit,
    navController: NavController
) {
    val registerViewModel: RegisterViewModel = hiltViewModel()
    val context = LocalContext.current

    var login by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    var showPassword by rememberSaveable { mutableStateOf(false) }

    var loginError by rememberSaveable { mutableStateOf(false) }
    var emailError by rememberSaveable { mutableStateOf(false) }
    var passwordError by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordError by rememberSaveable { mutableStateOf(false) }


    val isLoading by registerViewModel.isLoading.collectAsState()
    val errorMessage by registerViewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .border(width = 2.dp, color = colorScheme.primary, shape = RoundedCornerShape(16.dp)),
        verticalArrangement = Arrangement.Center,
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
                loginError = false
                emailError = false
                passwordError = false
                confirmPasswordError = false

                when {
                    !RegisterValidator.validateLogin(login, context) -> loginError = true
                    !RegisterValidator.validateEmail(email, context) -> emailError = true
                    !RegisterValidator.validatePassword(password, context) -> passwordError = true
                    !RegisterValidator.validatePasswordConfirmation(
                        password,
                        confirmPassword,
                        context
                    ) -> confirmPasswordError = true

                    else -> registerViewModel.register(login, email, password, confirmPassword) {
                        onNext()
                    }
                }
            },
            text = if (isLoading)
                Lang.string(R.string.register_button_sending)
            else
                Lang.string(R.string.register_button_register),
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )

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