package com.example.sharkflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sharkflow.ui.components.Accordion

@Composable
fun FAQScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(20.dp, 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            "Часто задаваемые вопросы",
            color = Color.Blue,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium,
        )

        Text(
            text = "SharkFlow — даёт ответы на все частые вопросы пользователей",
            textAlign = TextAlign.Center
        )

        Accordion(
            "Каким образом обеспечивается шифрование пользовательских данных?",
            "Все данные передаются по протоколу HTTPS с TLS-шифрованием, а пароли хранятся в базе только в виде bcrypt-хэшей с солью."
        )
        Accordion(
            "Кто имеет доступ к информации в базе данных?",
            "Доступ строго ограничен: только сервисные компоненты и администраторы с многофакторной аутентификацией могут работать с данными."
        )
        Accordion(
            "Как часто проводятся резервные копии и где они хранятся?",
            "Резервное копирование выполняется каждые 6 часов и хранится в геораспределённых хранилищах с шифрованием на уровне объекта."
        )
        Accordion(
            "Как осуществляется мониторинг и обнаружение аномалий?",
            "Система журналирования интегрирована с SIEM-платформой для круглосуточного мониторинга и автоматического оповещения о подозрительной активности."
        )
        Accordion(
            "Какие меры принимаются для защиты от DDoS-атак?",
            "Используется распределённая сеть CDN с встроенным WAF и автоматическим масштабированием для фильтрации и погашения целевых нагрузок."
        )
        Accordion(
            "Где хранятся серверы и соответствуют ли они стандартам безопасности?",
            "Серверы находятся в сертифицированных дата-центрах Tier III, соответствующих стандартам ISO 27001 и SOC 2."
        )
        Accordion(
            "Как обрабатываются инциденты и уведомления о нарушениях?",
            "В случае инцидента включается процедура ICS: уведомление команды реагирования, анализ, устранение и отчёт в течение 24 часов."
        )
        Accordion(
            "Какие гарантии конфиденциальности предоставляются пользователю?",
            "Мы обязуемся не передавать данные третьим лицам и использовать их исключительно в рамках оказания услуг, согласно нашей политике GDPR."
        )
        Accordion(
            "Как реализована защита от несанкционированного доступа?",
            "Применяются политики строгих ролей, контроль сессий, блокировка по IP и ограничение числа попыток входа."
        )
        Accordion(
            "Как планируется масштабирование и обновление системы?",
            "Обновления внедряются через CI/CD-пайплайн с Canary-релизами и автоматическими нагрузочными тестами, чтобы не нарушать работу сервиса."
        )

    }
}
