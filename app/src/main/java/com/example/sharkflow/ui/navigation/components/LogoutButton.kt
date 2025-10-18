package com.example.sharkflow.ui.navigation.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sharkflow.R
import com.example.sharkflow.utils.Lang

@Composable
fun LogoutButton(
    navController: NavHostController,
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(colorScheme.error)
            .padding(top = 6.dp)
            .fillMaxSize()
            .clickable { showDialog = true }
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Logout,
            tint = colorScheme.onPrimary,
            modifier = Modifier.size(26.dp),
            contentDescription = Lang.string(R.string.logout_button_desc)
        )
        Text(
            text = Lang.string(R.string.logout_button_text),
            color = colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.labelSmall.fontSize,
        )
    }

    if (showDialog) {
        LogoutModal(onDismiss = { showDialog = false }, navController)
    }

}
