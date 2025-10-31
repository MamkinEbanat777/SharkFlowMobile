package com.example.sharkflow.presentation.screens.task.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.sharkflow.core.common.DateUtils
import com.example.sharkflow.core.common.DateUtils.formatDateTimeReadable
import com.example.sharkflow.core.presentation.toUi
import com.example.sharkflow.domain.model.Task
import java.time.*
import java.util.concurrent.TimeUnit

@Composable
fun TaskRow(
    task: Task,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val statusUi = remember(task.status) { task.status.toUi() }
    val priorityUi = remember(task.priority) { task.priority.toUi() }

    val statusColor = statusUi.color
    val priorityColor = priorityUi.color

    val syncColor =
        if (task.isSynced) colorScheme.secondary else colorScheme.error

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.primary)
    ) {
        Box(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = task.description ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(vertical = 4.dp),
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    task.dueDate?.let { rawDate ->
                        val dueInstant = DateUtils.parseToInstant(rawDate)
                        val dueDateFormatted = formatDateTimeReadable(rawDate) ?: rawDate

                        val now = ZonedDateTime.now()
                        val dueZdt = dueInstant?.atZone(ZoneId.systemDefault())
                        val dueDateColor =
                            if (dueZdt != null && dueZdt.isBefore(now)) colorScheme.error
                            else colorScheme.onSurface

                        val isDeadlineSoon = dueZdt?.let {
                            val diff =
                                it.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()
                            diff in 1 until TimeUnit.HOURS.toMillis(24)
                        } ?: false

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = dueDateFormatted,
                                style = MaterialTheme.typography.labelSmall,
                                color = dueDateColor,
                                modifier = Modifier
                                    .background(
                                        color = colorScheme.background,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )

                            if (isDeadlineSoon) {
                                Spacer(modifier = Modifier.width(4.dp))
                                val pulseColor = colorScheme.error.copy(alpha = 0.6f)

                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    val infiniteTransition = rememberInfiniteTransition(label = "")
                                    val pulse by infiniteTransition.animateFloat(
                                        initialValue = 0f,
                                        targetValue = 1f,
                                        animationSpec = infiniteRepeatable(
                                            animation = tween(
                                                durationMillis = 1500,
                                                easing = LinearOutSlowInEasing
                                            ),
                                            repeatMode = RepeatMode.Restart
                                        ),
                                        label = ""
                                    )

                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        val maxRadius = size.minDimension / 2f
                                        drawCircle(
                                            color = pulseColor.copy(alpha = 0.6f * (1 - pulse)),
                                            radius = maxRadius * (0.3f + pulse * 1.2f),
                                            center = center
                                        )
                                    }

                                    Icon(
                                        imageVector = Icons.Default.AccessAlarm,
                                        contentDescription = "Скоро дедлайн",
                                        tint = colorScheme.error,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            task.status.displayName(),
                            style = MaterialTheme.typography.labelSmall,
                            color = statusColor,
                            modifier = Modifier
                                .background(
                                    color = colorScheme.background,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )

                        Text(
                            task.priority.displayName(),
                            style = MaterialTheme.typography.labelSmall,
                            color = priorityColor,
                            modifier = Modifier
                                .background(
                                    color = colorScheme.background,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )

                        Text(
                            text = if (task.isSynced) "Синхронизировано" else "Не синхронизировано",
                            style = MaterialTheme.typography.labelSmall,
                            color = syncColor,
                            modifier = Modifier
                                .background(
                                    color = colorScheme.background,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = "Создано",
                                tint = colorScheme.onPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = task.createdAt?.let { formatDateTimeReadable(it) }
                                    ?: "—",
                                style = MaterialTheme.typography.bodySmall,
                                color = colorScheme.onPrimary
                            )

                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Update,
                                contentDescription = "Обновлено",
                                tint = colorScheme.onPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = task.updatedAt?.let { formatDateTimeReadable(it) }
                                    ?: "—",
                                style = MaterialTheme.typography.bodySmall,
                                color = colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 4.dp)
                    .wrapContentSize(),
            ) {
                IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Редактировать",
                        tint = colorScheme.onPrimary
                    )
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить",
                        tint = colorScheme.error
                    )
                }
            }
        }
    }
}
