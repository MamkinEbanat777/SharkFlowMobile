package com.example.sharkflow.presentation.navigation.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.sharkflow.R
import com.example.sharkflow.core.common.Lang
import com.example.sharkflow.presentation.common.ConfirmationModal
import com.example.sharkflow.presentation.screens.auth.viewmodel.AuthStateViewModel

@Composable
fun LogoutModal(
    onDismiss: () -> Unit,
    navController: NavHostController,
    authStateViewModel: AuthStateViewModel
) {
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
        },
        confirmButtonText = Lang.string(R.string.logout_button_yes),
        dismissButtonText = Lang.string(R.string.common_no)
    )
}
