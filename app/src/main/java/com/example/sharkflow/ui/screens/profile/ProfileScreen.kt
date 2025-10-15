package com.example.sharkflow.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import com.example.sharkflow.model.UserResponse

@Composable
fun ProfileScreen(currentUser: UserResponse?) {
    if (currentUser == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "${currentUser.login}", style = MaterialTheme.typography.titleMedium)
            Text(text = "${currentUser.email}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
