package com.example.sharkflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.FileDownloadOff
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
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
import com.example.sharkflow.ui.components.Link

@Composable
fun AdvantagesScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Преимущества",
            color = Color.Blue,
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = "SharkFlow — надёжный инструмент для управления задачами",
            textAlign = TextAlign.Center
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.MoneyOff,
                contentDescription = "Бесплатное использование",
                modifier = Modifier.size(24.dp)
            )
            Text("1. Бесплатное использование", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
        Text("Полный доступ ко всем функциям без оплаты.")

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.OpenWith,
                contentDescription = "Открытый исходный код",
                modifier = Modifier.size(24.dp)
            )
            Text("2. Открытый исходный код", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
        Link(
            fullText = "Проект полностью open-source и легко модифицируем.",
            linkText = "open-source",
            url = "https://github.com/Kramarich000/SharkFlow",
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.AdsClick,
                contentDescription = "Простота",
                modifier = Modifier.size(24.dp)
            )
            Text("3. Простота", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
        Text("Интерфейс, понятный даже без обучения.")

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Security,
                contentDescription = "Безопасность",
                modifier = Modifier.size(24.dp)
            )
            Text("4. Безопасность", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
        Text("Никакой внешней аналитики — ваши данные под контролем.")

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.FileDownloadOff,
                contentDescription = "Работа без установки",
                modifier = Modifier.size(24.dp)
            )
            Text("5. Работа без установки", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
        Text("Запускается прямо в браузере, даже на мобильных.")
    }
}
