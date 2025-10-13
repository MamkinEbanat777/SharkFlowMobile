package com.example.sharkflow.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.hilt.lifecycle.viewmodel.compose.*
import androidx.navigation.*
import com.example.sharkflow.ui.components.*
import com.example.sharkflow.viewmodel.*

@Composable
fun LoginScreen(
    navController: NavController,
) {
    val authStateViewModel: AuthStateViewModel = hiltViewModel()

    val isLoggedIn by remember { derivedStateOf { authStateViewModel.isLoggedIn } }

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate("dashboard") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    if (!isLoggedIn) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            LoginForm(navController)
        }
    }

}
