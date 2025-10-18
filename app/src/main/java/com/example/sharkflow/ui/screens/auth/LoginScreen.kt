package com.example.sharkflow.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sharkflow.ui.screens.auth.components.LoginForm
import com.example.sharkflow.ui.screens.profile.viewmodel.UserProfileViewModel

@Composable
fun LoginScreen(
    navController: NavController
) {
    val userProfileViewModel: UserProfileViewModel = hiltViewModel()
    val currentUser by userProfileViewModel.currentUser.collectAsState(initial = null)

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            navController.navigate("dashboard") {
                popUpTo("login") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        LoginForm(navController)
    }
}
