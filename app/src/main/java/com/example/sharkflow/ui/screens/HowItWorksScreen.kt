package com.example.sharkflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HowItWorksScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(0.dp, 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Text(
            text = "Как это работает",
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            color = Color.Blue,
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
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Color.Blue, shape = RoundedCornerShape(30.dp))
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
