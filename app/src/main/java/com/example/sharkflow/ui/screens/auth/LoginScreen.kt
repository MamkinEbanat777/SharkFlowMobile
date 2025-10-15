package com.example.sharkflow.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sharkflow.ui.components.LoginForm
import com.example.sharkflow.viewmodel.AuthStateViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    authStateViewModel: AuthStateViewModel
) {
    val isLoggedIn by remember { derivedStateOf { authStateViewModel.isLoggedIn } }

    if (!isLoggedIn) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            LoginForm(navController, authStateViewModel)
        }
    }
}
