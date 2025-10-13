package com.example.sharkflow.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import com.example.sharkflow.ui.components.*

@Composable
fun SupportScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Поддержка и обратная связь",
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            color = colorScheme.primary
        )
        Text(
            text = "Мы открыты для связи и готовы помочь.",
            textAlign = TextAlign.Center
        )
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = "Если у вас возникли вопросы, предложения или технические сложности — свяжитесь с нами удобным способом:",
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text("E-mail: support@sharkflow.app")
                Text("Telegram: @sharkflow_support")
                Text("Discord: discord.gg/sharkflow")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Если же у вас специфичный вопрос — заполните форму:",
            )
            ContactForm()
        }
    }
}
