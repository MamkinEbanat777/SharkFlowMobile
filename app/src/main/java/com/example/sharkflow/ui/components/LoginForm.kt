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
import com.example.sharkflow.ui.screens.auth.viewmodel.LoginViewModel
import com.example.sharkflow.utils.ToastManager
import com.example.sharkflow.viewmodel.AuthStateViewModel

@Composable
fun LoginForm(
    navController: NavController,
    authStateViewModel: AuthStateViewModel
) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val emptyFieldWarning = Lang.string(R.string.login_empty_fields_warning)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    ObserveToastMessages(
        errorMessage = loginViewModel.errorMessage,
        successMessage = loginViewModel.successMessage
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
            onValueChange = { email = it },
            label = Lang.string(R.string.login_email_label),
        )

        AppField(
            value = password,
            onValueChange = { password = it },
            label = Lang.string(R.string.login_password_label),
            isPassword = true,
            showPassword = passwordVisible,
            onToggleVisibility = { passwordVisible = !passwordVisible }
        )

        loginViewModel.errorMessage?.let {
            Text(it, color = colorScheme.error)
        }

        AppButton(
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    ToastManager.warning(
                        context,
                        emptyFieldWarning
                    )
                } else {
                    loginViewModel.login(email, password) { user ->
                        authStateViewModel.setUser(user)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            variant = AppButtonVariant.Primary,
            text = if (loginViewModel.isLoading)
                Lang.string(R.string.login_button_sending)
            else
                Lang.string(R.string.login_button_send),
            enabled = !loginViewModel.isLoading
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