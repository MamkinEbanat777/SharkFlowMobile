package com.example.sharkflow.core.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.sharkflow.domain.model.*

data class StatusUi(val color: Color, val icon: ImageVector, val backgroundAlpha: Float = 0.1f)

fun TaskStatus.toUi(): StatusUi = when (this) {
    TaskStatus.PENDING -> StatusUi(Color.Gray, Icons.Filled.Schedule)
    TaskStatus.IN_PROGRESS -> StatusUi(Color(0xFFFFC107), Icons.Filled.Timer)
    TaskStatus.COMPLETED -> StatusUi(Color(0xFF4CAF50), Icons.Filled.CheckCircle)
    TaskStatus.CANCELLED -> StatusUi(Color(0xFFF44336), Icons.Filled.Cancel)
}

data class PriorityUi(val color: Color, val icon: ImageVector, val backgroundAlpha: Float = 0.1f)

fun TaskPriority.toUi(): PriorityUi = when (this) {
    TaskPriority.LOW -> PriorityUi(Color(0xFFB0BEC5), Icons.Filled.KeyboardArrowDown)
    TaskPriority.MEDIUM -> PriorityUi(Color(0xFFFFB74D), Icons.Filled.KeyboardArrowUp)
    TaskPriority.HIGH -> PriorityUi(Color(0xFFD32F2F), Icons.Filled.PriorityHigh)
}
