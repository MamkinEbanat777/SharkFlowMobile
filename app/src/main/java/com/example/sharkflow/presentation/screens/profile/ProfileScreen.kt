package com.example.sharkflow.presentation.screens.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sharkflow.presentation.common.*
import com.example.sharkflow.presentation.screens.auth.viewmodel.AuthStateViewModel
import com.example.sharkflow.presentation.screens.profile.components.*
import com.example.sharkflow.presentation.screens.profile.viewmodel.UserProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    userProfileViewModel: UserProfileViewModel,
    authStateViewModel: AuthStateViewModel
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showCodeDeleteDialog by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showUserSessionsDialog by remember { mutableStateOf(false) }

    val currentUser by userProfileViewModel.currentUser.collectAsState()
    val isRefreshing by userProfileViewModel.isRefreshing.collectAsState()

    if (currentUser == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = colorScheme.primary)
        }
        return
    }

    AppSwipeRefresh(
        isRefreshing = isRefreshing,
        onRefresh = { userProfileViewModel.refreshUser() }
    ) {
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

            currentUser?.login?.let {
                Text(
                    text = it,
                    style = typography.titleLarge,
                    color = colorScheme.onBackground
                )
            }
            currentUser?.email?.let {
                Text(
                    text = it,
                    style = typography.bodyMedium,
                    color = colorScheme.onBackground.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            AppButton(
                onClick = { showUpdateDialog = true },
                modifier = Modifier.fillMaxWidth(0.8f),
                icon = (Icons.Filled.ModeEditOutline),
                text = "Обновить профиль"
            )

            Spacer(modifier = Modifier.height(8.dp))

            AppButton(
                variant = AppButtonVariant.Outlined,
                tone = AppButtonTone.Danger,
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth(0.8f),
                text = "Удалить профиль",
                icon = (Icons.Filled.Delete)
            )

            Spacer(modifier = Modifier.height(8.dp))

            AppButton(
                onClick = { showUserSessionsDialog = true },
                icon = (Icons.Filled.Devices),
                text = "Просмотреть активные сессии"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Здесь вы можете обновить свои данные или удалить аккаунт, а также просмотреть активные сессии.",
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

        if (showUserSessionsDialog) {
            UserSessionsModal(
                onDismiss = { showUserSessionsDialog = false },
                userProfileViewModel = userProfileViewModel,
                authStateViewModel = authStateViewModel
            )
        }
    }
}