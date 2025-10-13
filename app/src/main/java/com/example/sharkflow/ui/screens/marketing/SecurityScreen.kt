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
fun SecurityScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Безопасность",
            style = MaterialTheme.typography.displayMedium,
            color = colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Text(
            text = "SharkFlow — защищает ваши данные на всех уровнях",
            textAlign = TextAlign.Center
        )
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Text("Шифрование паролей", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(
                "Пароли пользователей никогда не хранятся в открытом виде — используется bcrypt с солью.",
            )

            Text("Защищённое соединение", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(
                "Все данные передаются по HTTPS с TLS-шифрованием, исключая возможность перехвата.",
            )

            Text("Доступ к базе", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(
                "Доступ к данным имеют только авторизованные пользователи и сервисы с ограниченными правами.",
            )
            
            Text("Конфиденциальность", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(
                "Мы не передаём, не продаём и не используем персональные данные в сторонних целях.",
            )

            Text("Резервное копирование", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(
                "База данных регулярно резервируется для защиты от сбоев и потери информации.",
            )
        }
    }
}