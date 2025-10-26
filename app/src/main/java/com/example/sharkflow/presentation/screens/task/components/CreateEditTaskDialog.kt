package com.example.sharkflow.presentation.screens.task.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.sharkflow.data.api.dto.task.*
import com.example.sharkflow.utils.DateUtils
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditTaskDialog(
    initialTitle: String = "",
    initialDescription: String? = "",
    initialDueDate: String? = null,
    initialStatus: Status? = Status.PENDING,
    initialPriority: Priority? = Priority.MEDIUM,
    onDismiss: () -> Unit,
    onConfirm: (
        title: String,
        description: String?,
        dueDate: String?,
        status: Status?,
        priority: Priority?
    ) -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var description by remember { mutableStateOf(initialDescription ?: "") }
    var status by remember { mutableStateOf(initialStatus ?: Status.PENDING) }
    var priority by remember { mutableStateOf(initialPriority ?: Priority.MEDIUM) }

    var selectedDateMillis by remember {
        mutableLongStateOf(DateUtils.parseToInstant(initialDueDate)?.toEpochMilli() ?: 0L)
    }
    var dueDateIso by remember {
        mutableStateOf(DateUtils.toServerInstantString(initialDueDate) ?: "")
    }

    val initialLocalTime = remember(initialDueDate) {
        DateUtils.parseToInstant(initialDueDate)
            ?.atZone(ZoneId.systemDefault())
            ?.toLocalTime()
            ?: LocalTime.MIDNIGHT
    }

    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()) }
    val dateTimeFormatter =
        remember { DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm", Locale.getDefault()) }

    val dueDateText by remember(selectedDateMillis) {
        derivedStateOf {
            if (selectedDateMillis > 0L) {
                val z = Instant.ofEpochMilli(selectedDateMillis).atZone(ZoneId.systemDefault())
                val time = z.toLocalTime()
                if (time != LocalTime.MIDNIGHT) z.format(dateTimeFormatter)
                else z.toLocalDate().format(dateFormatter)
            } else "Не выбрано"
        }
    }

    var showCalendarDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDateMillis.takeIf { it > 0L }
    )

    // Time picker state
    val timePickerState = rememberTimePickerState(
        initialHour = initialLocalTime.hour,
        initialMinute = initialLocalTime.minute,
        is24Hour = true
    )
    var showTimeDialog by remember { mutableStateOf(false) }

    LaunchedEffect(showTimeDialog) {
        if (showTimeDialog) {
            val inst = if (selectedDateMillis > 0L) {
                Instant.ofEpochMilli(selectedDateMillis)
                    .atZone(ZoneId.systemDefault()).toLocalTime()
            } else initialLocalTime
            timePickerState.hour = inst.hour
            timePickerState.minute = inst.minute
        }
    }

    fun openCalendar() {
        datePickerState.selectedDateMillis =
            selectedDateMillis.takeIf { it > 0L } ?: System.currentTimeMillis()
        showCalendarDialog = true
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialTitle.isEmpty()) "Создание задачи" else "Редактирование задачи") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Дедлайн",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(6.dp))
                        .clickable { openCalendar() }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = dueDateText, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Календарь")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Статус
                var statusExpanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedTextField(
                        value = status.displayName(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Статус") },
                        trailingIcon = {
                            IconButton(onClick = { statusExpanded = !statusExpanded }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = { statusExpanded = false },
                        properties = PopupProperties(focusable = false)
                    ) {
                        Status.entries.forEach { s ->
                            DropdownMenuItem(text = { Text(s.displayName()) }, onClick = {
                                status = s
                                statusExpanded = false
                            })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Приоритет
                var priorityExpanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedTextField(
                        value = priority.displayName(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Приоритет") },
                        trailingIcon = {
                            IconButton(onClick = { priorityExpanded = !priorityExpanded }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = priorityExpanded,
                        onDismissRequest = { priorityExpanded = false },
                        properties = PopupProperties(focusable = false)
                    ) {
                        Priority.entries.forEach { p ->
                            DropdownMenuItem(text = { Text(p.displayName()) }, onClick = {
                                priority = p
                                priorityExpanded = false
                            })
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val sendDue = dueDateIso.ifBlank { null }
                onConfirm(title.trim(), description.trim(), sendDue, status, priority)
            }) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )

    // DatePickerDialog — показываем только если нужно
    if (showCalendarDialog) {
        DatePickerDialog(
            onDismissRequest = { showCalendarDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        // datePickerState обычно даёт millis на startOfDay(system)
                        selectedDateMillis = millis
                        // сразу открываем выбор времени
                        showTimeDialog = true
                    }
                    showCalendarDialog = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showCalendarDialog = false
                }) { Text("Отмена") }
            }
        ) {
            DatePicker(state = datePickerState, showModeToggle = false)
        }
    }

    // TimePickerDialog — показываем после выбора даты
    if (showTimeDialog) {
        TimePickerDialog(
            onDismissRequest = { showTimeDialog = false },
            title = { Text("Выберите время") },
            confirmButton = {
                TextButton(onClick = {
                    val localDate = Instant.ofEpochMilli(selectedDateMillis)
                        .atZone(ZoneId.systemDefault()).toLocalDate()
                    val chosenLocalDateTime = LocalDateTime.of(
                        localDate,
                        LocalTime.of(timePickerState.hour, timePickerState.minute)
                    )
                    val instant = chosenLocalDateTime.atZone(ZoneId.systemDefault()).toInstant()
                    selectedDateMillis = instant.toEpochMilli()
                    dueDateIso = instant.toString()
                    showTimeDialog = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showTimeDialog = false }) { Text("Отмена") } }
        ) {
            TimePicker(state = timePickerState)
        }
    }
}
