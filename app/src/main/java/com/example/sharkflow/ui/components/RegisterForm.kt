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

@Composable
fun RegisterForm(
    onNext: () -> Unit,
    navController: NavController
) {
    val registerViewModel: RegisterViewModel = hiltViewModel()

    var login by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showPassword by remember { mutableStateOf(false) }
    var showEmptyFieldsWarning by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 2.dp, color = colorScheme.primary, shape = RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Регистрация",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 20.dp)
        )
        AppField(
            value = login,
            onValueChange = { login = it },
            label = "Введите логин"
        )

        AppField(
            value = email,
            onValueChange = { email = it },
            label = "Введите почту"
        )

        AppField(
            value = password,
            onValueChange = { password = it },
            label = "Введите пароль",
            isPassword = true,
            showPassword = showPassword,
            onToggleVisibility = { showPassword = !showPassword }
        )

        AppField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Подтвердите пароль",
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
            text = (if (registerViewModel.isLoading) "Отправка..." else "Зарегистрироваться"),
            enabled = !registerViewModel.isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        if (showEmptyFieldsWarning) {
            ToastManager.warning(context, "Не все поля заполнены!")
            /*Text(А
                text = "Пожалуйста, заполните все поля",
                color = colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )*/
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Уже есть аккаунт?")
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(onClick = { navController.navigate("login") }) {
                Text("Войти", color = colorScheme.primary)
            }
        }
    }
}