package com.example.sharkflow.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sharkflow.R

@Composable
fun AboutScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(20.dp, 40.dp),
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
            "Специалист по созданию и поддержке серверной логики и баз данных. Эксперт в Node.js и PostgreSQL.",
            "Отвечает за инфраструктуру, CI/CD и безопасность. Внедряет автоматизацию и поддерживает стабильность сервисов.",
            "Создаёт удобные и понятные интерфейсы, улучшая пользовательский опыт. Работает над визуальной частью и прототипами."
        )

        Text(
            "О нас",
            color = Color.Blue,
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
                modifier = Modifier.fillMaxWidth()
            ) {
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
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Text(
                    text = member.second,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Text(
                    text = descriptions[index],
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
        }
    }
}
