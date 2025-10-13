package com.example.sharkflow.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*

@Composable
fun RegistrationSuccess() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Успех",
            tint = colorScheme.onPrimary,
            modifier = Modifier
                .size(120.dp)
                .padding(20.dp)
                .background(
                    color = colorScheme.primary,
                    shape = RoundedCornerShape(100)
                )
        )
        Text(
            text = "Вы успешно зарегистрировались",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displaySmall
        )
    }
}
