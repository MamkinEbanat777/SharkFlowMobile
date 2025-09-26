package com.example.sharkflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun FeaturesScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Text(
            "Ключевые особенности",
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            color = Color.Blue,
        )

        Text(
            text = "SharkFlow — функциональный инструмент с полезными фишками",
            textAlign = TextAlign.Center
        )

        val features = listOf(
            "Планирование задач" to "Создавайте и распределяйте задачи, устанавливайте приоритеты и сроки — всё в одном месте.",
            "Командная работа" to "Общайтесь, назначайте роли и следите за вкладом каждого участника в проект.",
            "Напоминания/дедлайны" to "Не упускайте важные сроки — автоматические уведомления всегда напомнят о приближении дедлайна.",
            "Прогресс и метрики" to "Отслеживайте выполнение задач, анализируйте эффективность и улучшайте рабочие процессы."
        )

        features.forEach { (title, desc) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Text(desc, textAlign = TextAlign.Center)
            }
        }
    }
}
