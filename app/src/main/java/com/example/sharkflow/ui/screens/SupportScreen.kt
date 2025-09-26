package com.example.sharkflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sharkflow.ui.components.ContactForm

@Composable
fun SupportScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Поддержка и обратная связь",
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            color = Color.Blue
        )
        Text(
            text = "Мы открыты для связи и готовы помочь.",
            textAlign = TextAlign.Center
        )
        Text(
            text = "Если у вас возникли вопросы, предложения или технические сложности — свяжитесь с нами удобным способом:",
            textAlign = TextAlign.Center
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("E-mail: support@sharkflow.app")
            Text("Telegram: @sharkflow_support")
            Text("Discord: discord.gg/sharkflow")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Если же у вас специфичный вопрос — заполните форму:",
            textAlign = TextAlign.Center
        )
        ContactForm()
    }
}
