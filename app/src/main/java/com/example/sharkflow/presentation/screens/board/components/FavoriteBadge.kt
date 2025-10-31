package com.example.sharkflow.presentation.screens.board.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun FavoriteBadge(isFavorite: Boolean) {
    if (!isFavorite) return
    val syncIcon: ImageVector = Icons.Filled.Star

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay((60L).coerceAtMost(400L))
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
            color = Color(0xFFFFD700).copy(alpha = 0.15f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    syncIcon,
                    contentDescription = null,
                    tint = Color(0xFFB8860B),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "В избранном",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFB8860B),
                )
            }
        }

    }
}