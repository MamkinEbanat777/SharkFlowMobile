package com.example.sharkflow.presentation.screens.task.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.sharkflow.core.presentation.toUi
import com.example.sharkflow.domain.model.TaskPriority
import kotlinx.coroutines.delay

@Composable
fun PriorityBadge(priority: TaskPriority) {
    val ui = priority.toUi()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay((120L).coerceAtMost(400L))
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(280)) +
                scaleIn(initialScale = 0.96f, animationSpec = tween(280)) +
                slideInVertically(initialOffsetY = { it / 6 }, animationSpec = tween(280)),
        exit = fadeOut() + scaleOut() + slideOutVertically()
    ) {
        Surface(
            color = ui.color.copy(alpha = ui.backgroundAlpha),
            shape = RoundedCornerShape(12.dp),
        ) {
            Row(
                modifier = Modifier.Companion.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                Icon(
                    ui.icon,
                    contentDescription = null,
                    tint = ui.color,
                    modifier = Modifier.Companion.size(16.dp)
                )
                Spacer(modifier = Modifier.Companion.width(4.dp))
                Text(
                    text = priority.displayName(),
                    color = ui.color,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}