package com.example.sharkflow.presentation.screens.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sharkflow.R
import com.example.sharkflow.core.common.Lang

@Composable
fun RegistrationSuccess() {
    val successText = Lang.string(R.string.registration_success_message)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = successText,
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
            text = successText,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displaySmall
        )
    }
}
