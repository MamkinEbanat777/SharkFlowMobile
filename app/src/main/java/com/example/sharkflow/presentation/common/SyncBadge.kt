package com.example.sharkflow.presentation.common

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.sharkflow.presentation.theme.SuccessColor
import kotlinx.coroutines.delay

@Composable
fun SyncBadge(isSynced: Boolean) {
    val syncColor =
        if (isSynced) SuccessColor else colorScheme.error
    val syncIcon = if (isSynced) Icons.Filled.Sync else Icons.Filled.SyncDisabled

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
            color = syncColor.copy(.1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.Companion.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                Icon(
                    syncIcon,
                    contentDescription = null,
                    tint = syncColor,
                    modifier = Modifier.Companion.size(16.dp)
                )
                Spacer(modifier = Modifier.Companion.width(4.dp))
                Text(
                    text = if (isSynced) "Синхронизировано" else "Не синхронизировано",
                    style = MaterialTheme.typography.labelSmall,
                    color = syncColor,
                )
            }
        }
    }
}