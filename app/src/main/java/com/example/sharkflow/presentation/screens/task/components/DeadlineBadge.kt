package com.example.sharkflow.presentation.screens.task.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.time.*

@Composable
fun DeadlineBadge(dueDate: String?) {
    val remainingTime = remember(dueDate) { mutableStateOf("-") }

    val dueInstant = remember(dueDate) {
        runCatching { Instant.parse(dueDate) }.getOrNull()
    }

    if (dueInstant != null) {
        LaunchedEffect(dueInstant) {
            while (true) {
                val now = Instant.now()
                val diff = Duration.between(now, dueInstant)
                val totalSeconds = diff.seconds

                val days = totalSeconds / 86400
                val hours = (totalSeconds % 86400) / 3600
                val minutes = (totalSeconds % 3600) / 60
                val seconds = totalSeconds % 60

                remainingTime.value = when {
                    totalSeconds < 0 -> "Просрочено"
                    days > 0 -> "$days д $hours ч"
                    hours > 0 -> "$hours ч $minutes мин"
                    minutes > 0 -> "$minutes мин $seconds сек"
                    else -> "$seconds сек"
                }

                val delayMillis = when {
                    totalSeconds < 0 -> 60_000L
                    totalSeconds <= 3600 -> 1000L
                    else -> 60_000L
                }
                delay(delayMillis)
            }
        }
    }

    val backgroundColor = if (remainingTime.value == "Просрочено") Color(0xFFFF5252)
    else Color(0xFFBBDEFB)

    Box(
        modifier = Modifier.Companion
            .background(backgroundColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = remainingTime.value,
            style = MaterialTheme.typography.bodyMedium,
            color = if (remainingTime.value == "Просрочено") Color.Companion.White else Color.Companion.Black
        )
    }
}