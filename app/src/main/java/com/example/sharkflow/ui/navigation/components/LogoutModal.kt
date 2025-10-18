package com.example.sharkflow.ui.navigation.components

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.sharkflow.R
import com.example.sharkflow.ui.common.ConfirmationModal
import com.example.sharkflow.ui.screens.auth.viewmodel.AuthStateViewModel
import com.example.sharkflow.utils.Lang

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
        },
        confirmButtonText = Lang.string(R.string.logout_button_yes),
        dismissButtonText = Lang.string(R.string.common_no)
    )
}
