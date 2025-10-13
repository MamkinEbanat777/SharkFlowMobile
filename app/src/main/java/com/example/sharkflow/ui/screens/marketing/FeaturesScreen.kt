package com.example.sharkflow.ui.screens.marketing

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*

@Composable
fun FeaturesScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Text(
            "Ключевые особенности",
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            color = colorScheme.primary,
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
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(desc)
            }
        }
    }
}
