package com.example.sharkflow.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*

@Composable
fun ContactForm() {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var question by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 2.dp, color = colorScheme.primary, shape = RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.padding(top = 8.dp, start = 4.dp, end = 4.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = "Нужна помощь?", style = typography.headlineLarge,
                modifier = Modifier.padding(top = 20.dp)
            )
            AppField(
                value = email,
                onValueChange = { email = it },
                label = "Ваша почта",
            )

            AppField(
                value = name,
                onValueChange = { name = it },
                label = "Ваше имя",
            )

            AppField(
                value = question,
                onValueChange = { question = it },
                label = "Ваш вопрос",
                singleLine = false,
            )

            AppButton(
                onClick = { /* запрос типо*/ },
                variant = AppButtonVariant.Primary,
                text = "Отправить",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}