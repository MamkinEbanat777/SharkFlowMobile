package com.example.sharkflow.ui.screens.auth


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sharkflow.ui.screens.auth.components.*
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(
    navController: NavController,
) {
    var step by remember { mutableIntStateOf(1) }
    Column {
        Text(
            text = when (step) {
                1 -> "Шаг 1/3"
                2 -> "Шаг 2/3"
                3 -> "Шаг 3/3"
                else -> ""
            },
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(4.dp, top = 20.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        when (step) {
            1 -> RegisterForm(
                onNext = { step = 2 },
                navController = navController
            )

            2 -> CodeConfirmation(
                onNext = { step = 3 },
                onBack = { step = 1 })

            3 -> RegistrationSuccess()

        }
        if (step == 3) {
            LaunchedEffect(Unit) {
                delay(4000)
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
                    step = 1
                }

            }
        }
    }
}
