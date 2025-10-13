package com.example.sharkflow.ui.screens.marketing

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import com.example.sharkflow.ui.components.*

@Composable
fun AdvantagesScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Преимущества",
            color = colorScheme.primary,
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = "SharkFlow — надёжный инструмент для управления задачами",
            textAlign = TextAlign.Center
        )
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Row(
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
                url = "https://github.com/MamkinEbanat777/sharkflow",
            )

            Row(
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
}