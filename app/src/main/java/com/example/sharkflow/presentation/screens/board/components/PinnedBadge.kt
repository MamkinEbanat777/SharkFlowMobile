package com.example.sharkflow.presentation.screens.board.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun PinnedBadge(isPinned: Boolean) {
    if (!isPinned) return
    val syncIcon: ImageVector = Icons.Filled.PushPin

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
            color = colorScheme.primary.copy(.1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.Companion.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                Icon(
                    syncIcon,
                    contentDescription = null,
                    tint = colorScheme.primary,
                    modifier = Modifier.Companion.size(16.dp)
                )
                Spacer(modifier = Modifier.Companion.width(4.dp))
                Text(
                    text = if (isPinned) "Закреплена" else "",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.primary,
                )
            }
        }
    }
}
