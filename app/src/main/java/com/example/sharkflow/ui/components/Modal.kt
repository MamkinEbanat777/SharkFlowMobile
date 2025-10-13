package com.example.sharkflow.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import androidx.hilt.lifecycle.viewmodel.compose.*
import androidx.navigation.*
import com.example.sharkflow.viewmodel.*

@Composable
fun LogoutModal(
    onDismiss: () -> Unit,
    navController: NavHostController,
) {
    val authStateViewModel: AuthStateViewModel = hiltViewModel()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Подтверждение выхода", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Вы уверены, что хотите выйти из аккаунта?",
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) { Text("Нет") }
                    TextButton(onClick = {
                        authStateViewModel.logout() { success, _ ->
                            if (success) {
                                navController.navigate("login") {
                                    popUpTo("dashboard") { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        }
                        onDismiss()
                    }) { Text("Да") }
                }
            }
        }
    }
}

