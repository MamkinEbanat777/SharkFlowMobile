package com.example.sharkflow.ui.components

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.sharkflow.R
import com.example.sharkflow.data.local.language.Lang
import com.example.sharkflow.viewmodel.AuthStateViewModel

@Composable
fun LogoutModal(
    onDismiss: () -> Unit,
    navController: NavHostController,
) {
    val authStateViewModel: AuthStateViewModel = hiltViewModel()

    ConfirmationModal(
        title = Lang.string(R.string.logout_modal_title),
        message = Lang.string(R.string.logout_modal_message),
        onDismiss = onDismiss,
        onConfirm = {
            authStateViewModel.logout { success, _ ->
                if (success) {
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    )
}
