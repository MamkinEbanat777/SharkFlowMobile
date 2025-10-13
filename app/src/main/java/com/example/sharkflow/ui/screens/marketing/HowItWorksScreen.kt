package com.example.sharkflow.ui.screens.marketing

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*

@Composable
fun HowItWorksScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Text(
            text = "Как это работает",
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            color = colorScheme.primary,
        )

        Text(
            text = "SharkFlow — легко и понятно показывает все задачи и процессы",
            textAlign = TextAlign.Center
        )

        val steps = listOf(
            Triple("1", "Регистрация", "Создайте аккаунт за пару секунд"),
            Triple("2", "Добавление задачи", "Опишите задачу и установите дедлайн"),
            Triple("3", "Уведомления", "Получайте напоминания в нужный момент"),
            Triple("4", "Достижения", "Отмечайте завершённые и добивайтесь целей")
        )

        steps.forEach { (num, title, desc) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    num,
                    color = colorScheme.onPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(colorScheme.primary, shape = RoundedCornerShape(30.dp))
                        .padding(22.dp, 15.dp)
                )
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(desc, textAlign = TextAlign.Center)
            }
        }
    }
}
