package com.example.sharkflow.presentation.screens.task.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sharkflow.core.common.DateUtils
import com.example.sharkflow.core.presentation.AppDropdownField
import com.example.sharkflow.domain.model.*
import com.example.sharkflow.presentation.common.*
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditTaskDialog(
    initialTitle: String = "",
    initialDescription: String? = "",
    initialDueDate: String? = null,
    initialStatus: TaskStatus? = TaskStatus.PENDING,
    initialPriority: TaskPriority? = TaskPriority.MEDIUM,
    onDismiss: () -> Unit,
    onConfirm: (
        title: String,
        description: String?,
        dueDate: String?,
        status: TaskStatus?,
        priority: TaskPriority?
    ) -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var description by remember { mutableStateOf(initialDescription ?: "") }
    var status by remember { mutableStateOf(initialStatus ?: TaskStatus.PENDING) }
    var priority by remember { mutableStateOf(initialPriority ?: TaskPriority.MEDIUM) }

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
            } else "Срок"
        }
    }

    var showCalendarDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDateMillis.takeIf { it > 0L }
    )

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
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppField(title, { title = it }, "Название")

                AppField(description, { description = it }, "Описание")

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .height(56.dp)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onSurfaceVariant,
                            RoundedCornerShape(12.dp)
                        )
                        .background(
                            Color.Transparent,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { openCalendar() }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = dueDateText,
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = "Календарь",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }


                AppDropdownField(
                    value = status,
                    options = TaskStatus.entries,
                    label = "Статус",
                    valueText = { it.displayName() },
                    onOptionSelected = { status = it }
                )

                AppDropdownField(
                    value = priority,
                    options = TaskPriority.entries,
                    label = "Приоритет",
                    valueText = { it.displayName() },
                    onOptionSelected = { priority = it }
                )
            }
        },
        confirmButton = {
            AppButton(variant = AppButtonVariant.Text, onClick = {
                val sendDue = dueDateIso.ifBlank { null }
                onConfirm(title.trim(), description.trim(), sendDue, status, priority)
            }, text = "Сохранить")
        },
        dismissButton = {
            AppButton(variant = AppButtonVariant.Text, onClick = onDismiss, text = "Отмена")
        }
    )

    if (showCalendarDialog) {
        DatePickerDialog(
            onDismissRequest = { showCalendarDialog = false },
            confirmButton = {
                AppButton(variant = AppButtonVariant.Text, onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        selectedDateMillis = millis
                        showTimeDialog = true
                    }
                    showCalendarDialog = false
                }, text = "OK")
            },
            dismissButton = {
                AppButton(variant = AppButtonVariant.Text, onClick = {
                    showCalendarDialog = false
                }, text = "Отмена")
            }
        ) {
            DatePicker(state = datePickerState, showModeToggle = false)
        }
    }

    if (showTimeDialog) {
        TimePickerDialog(
            onDismissRequest = { showTimeDialog = false },
            title = { Text("Выберите время") },
            confirmButton = {
                AppButton(variant = AppButtonVariant.Text, onClick = {
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
                }, text = "OK")
            },
            dismissButton = {
                AppButton(
                    variant = AppButtonVariant.Text,
                    onClick = { showTimeDialog = false },
                    text = "Отмена"
                )
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }
}
