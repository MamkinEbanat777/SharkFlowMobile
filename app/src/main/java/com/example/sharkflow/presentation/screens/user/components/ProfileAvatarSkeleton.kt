package com.example.sharkflow.presentation.screens.user.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*

@Composable
fun ProfileAvatarSkeleton(
    modifier: Modifier = Modifier,
    size: Dp = 240.dp,
    borderColor: Color = colorScheme.primary,
    isUploading: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(colorScheme.surface.copy(alpha = alpha))
            .border(BorderStroke(3.dp, borderColor), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (isUploading) {
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = borderColor
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Default Avatar",
                tint = borderColor.copy(alpha = 0.5f),
                modifier = Modifier.size(size / 3)
            )
        }
    }
}
