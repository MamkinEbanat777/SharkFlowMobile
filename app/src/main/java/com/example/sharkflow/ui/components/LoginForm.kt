package com.example.sharkflow.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*
import androidx.hilt.lifecycle.viewmodel.compose.*
import androidx.navigation.*
import com.example.sharkflow.ui.screens.auth.viewmodel.*
import com.example.sharkflow.utils.*
import com.example.sharkflow.viewmodel.*

@Composable
fun LoginForm(
    navController: NavController,
) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val authStateViewModel: AuthStateViewModel = hiltViewModel()

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
            text = "Логин",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 20.dp)
        )

        AppField(
            value = email,
            onValueChange = { email = it },
            label = "Введите почту",
        )

        AppField(
            value = password,
            onValueChange = { password = it },
            label = "Введите пароль",
            isPassword = true,
            showPassword = passwordVisible,
            onToggleVisibility = { passwordVisible = !passwordVisible }
        )

        if (loginViewModel.errorMessage != null) {
            Text(loginViewModel.errorMessage!!, color = colorScheme.error)
        }

        AppButton(
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    ToastManager.warning(context, "Не все поля заполнены")
                } else {
                    loginViewModel.login(email, password) { user ->
                        authStateViewModel.setUser(user)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            variant = AppButtonVariant.Primary,
            text = (if (loginViewModel.isLoading) "Отправка..." else "Войти"),
            enabled = !loginViewModel.isLoading
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Нет аккаунта?")
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(onClick = { navController.navigate("register") }) {
                Text("Создать", color = colorScheme.primary)
            }
        }
    }
}