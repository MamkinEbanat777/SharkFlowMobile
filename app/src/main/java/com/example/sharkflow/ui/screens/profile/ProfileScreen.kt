package com.example.sharkflow.ui.screens.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sharkflow.ui.screens.profile.components.*
import com.example.sharkflow.ui.screens.profile.viewmodel.UserProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    userProfileViewModel: UserProfileViewModel
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showCodeDeleteDialog by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }

    val currentUser by userProfileViewModel.currentUser.collectAsState(initial = null)
    val isLoading = userProfileViewModel.isLoading

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = colorScheme.primary)
        }
        return
    }

    if (currentUser == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = colorScheme.primary)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileAvatar(
            userProfileViewModel = userProfileViewModel
        )

        Spacer(modifier = Modifier.height(16.dp))

        currentUser!!.login?.let {
            Text(
                text = it,
                style = typography.titleLarge,
                color = colorScheme.onBackground
            )
        }
        currentUser!!.email?.let {
            Text(
                text = it,
                style = typography.bodyMedium,
                color = colorScheme.onBackground.copy(alpha = 0.8f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { showUpdateDialog = true },
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(Icons.Filled.ModeEditOutline, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Обновить профиль")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { showDeleteDialog = true },
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = colorScheme.error
            ),
            border = BorderStroke(1.5.dp, colorScheme.error)
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Удалить профиль"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Удалить профиль", color = colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Здесь вы можете обновить свои данные или удалить аккаунт.",
            style = typography.bodySmall,
            color = colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )
    }

    if (showDeleteDialog) {
        ConfirmDeleteUserModal(
            onDismiss = { showDeleteDialog = false },
            userProfileViewModel = userProfileViewModel,
            onSuccess = {
                showDeleteDialog = false
                showCodeDeleteDialog = true
            }
        )
    }

    if (showCodeDeleteDialog) {
        DeleteUserModal(
            onDismiss = { showCodeDeleteDialog = false },
            userProfileViewModel
        ) {
            showCodeDeleteDialog = false
            navController.navigate("login") {
                popUpTo("dashboard") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    if (showUpdateDialog) {
        UpdateUserModal(
            onDismiss = { showUpdateDialog = false },
            userProfileViewModel = userProfileViewModel,
            onSuccess = {
                showUpdateDialog = false
            }
        )
    }
}
