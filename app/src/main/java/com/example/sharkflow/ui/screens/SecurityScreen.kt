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
import androidx.compose.ui.unit.sp

@Composable
fun SecurityScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Безопасность",
            style = MaterialTheme.typography.displayMedium,
            color = Color.Blue,
            textAlign = TextAlign.Center
        )

        Text(
            text = "SharkFlow — защищает ваши данные на всех уровнях",
            textAlign = TextAlign.Center
        )

        Text("Шифрование паролей", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(
            "Пароли пользователей никогда не хранятся в открытом виде — используется bcrypt с солью.",
            textAlign = TextAlign.Center
        )

        Text("Защищённое соединение", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(
            "Все данные передаются по HTTPS с TLS-шифрованием, исключая возможность перехвата.",
            textAlign = TextAlign.Center
        )

        Text("Доступ к базе", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(
            "Доступ к данным имеют только авторизованные пользователи и сервисы с ограниченными правами.",
            textAlign = TextAlign.Center
        )


        Text("Конфиденциальность", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(
            "Мы не передаём, не продаём и не используем персональные данные в сторонних целях.",
            textAlign = TextAlign.Center
        )


        Text("Резервное копирование", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(
            "База данных регулярно резервируется для защиты от сбоев и потери информации.",
            textAlign = TextAlign.Center
        )
    }
}
