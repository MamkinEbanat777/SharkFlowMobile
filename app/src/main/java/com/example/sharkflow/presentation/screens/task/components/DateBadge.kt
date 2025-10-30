package com.example.sharkflow.presentation.screens.task.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.sharkflow.core.common.DateUtils
import kotlinx.coroutines.delay

@Composable
fun DateBadge(title: String, date: String?, icon: ImageVector) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay((180L).coerceAtMost(400L))
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
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.Companion.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.Companion.size(16.dp))
                Spacer(modifier = Modifier.Companion.width(4.dp))
                Text(
                    text = "$title: ${DateUtils.formatDateTimeReadable(date) ?: "-"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}