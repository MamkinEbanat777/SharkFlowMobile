package com.example.sharkflow.ui.screens.marketing

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import com.example.sharkflow.R

@Composable
fun AboutScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        val team = listOf(
            Triple("Андрей Иванов", "Frontend-разработчик", R.drawable.dev1),
            Triple("Екатерина Смирнова", "Backend-разработчик", R.drawable.dev2),
            Triple("Игорь Петров", "DevOps-инженер", R.drawable.dev3),
            Triple("Ольга Васильева", "UI/UX-дизайнер", R.drawable.dev4)
        )

        val descriptions = listOf(
            "Отвечает за разработку и оптимизацию пользовательского интерфейса. Опыт в React и TypeScript более 5 лет.",
            "Специалист по созданию и поддержке серверной логики и баз данных. Эксперт в Node.js и Postgres.",
            "Отвечает за инфраструктуру, CI/CD и безопасность. Внедряет автоматизацию и поддерживает стабильность сервисов.",
            "Создаёт удобные и понятные интерфейсы, улучшая пользовательский опыт. Работает над визуальной частью и прототипами."
        )

        Text(
            "О нас",
            color = colorScheme.primary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium,
        )

        Text(
            text = "SharkFlow — команда профессионалов, создающая удобные сервисы",
            textAlign = TextAlign.Center
        )

        team.forEachIndexed { index, member ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorScheme.primary, shape = RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Image(
                        painter = painterResource(id = member.third),
                        contentDescription = member.first,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = member.first,
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onPrimary,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                    Text(
                        text = member.second,
                        textAlign = TextAlign.Center,
                        color = colorScheme.onPrimary,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                    Text(
                        text = descriptions[index],
                        textAlign = TextAlign.Center,
                        color = colorScheme.onPrimary,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }
            }
        }
    }
}
