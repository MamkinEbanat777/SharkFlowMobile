package com.example.sharkflow.presentation.screens.task.components

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
import com.example.sharkflow.core.system.AppLog
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
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(task.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    task.description ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                task.dueDate?.let { rawDate ->
                    val dueInstant = DateUtils.parseToInstant(rawDate)
                    val dueDateFormatted = formatDateTimeReadable(rawDate) ?: rawDate

                    AppLog.d("TaskRow", "rawDate='$rawDate' instant=$dueInstant")

                    val now = ZonedDateTime.now()
                    val dueZdt = dueInstant?.atZone(ZoneId.systemDefault())
                    val dueDateColor =
                        if (dueZdt != null && dueZdt.isBefore(now)) colorScheme.error
                        else colorScheme.onSurface

                    val isDeadlineSoon = dueZdt?.let {
                        val diff = it.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()
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
                            Icon(
                                imageVector = Icons.Default.AccessAlarm,
                                contentDescription = "Скоро дедлайн",
                                tint = colorScheme.error,
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(
                                        color = colorScheme.background,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                            )
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
                            text = task.updatedAt?.let { formatDateTimeReadable(it.toString()) }
                                ?: "—",
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.onPrimary
                        )
                    }
                }
            }

            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Редактировать")
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Удалить",
                        tint = colorScheme.error
                    )
                }
            }
        }
    }
}
