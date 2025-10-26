package com.example.sharkflow.presentation.screens.task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sharkflow.data.api.dto.task.*
import com.example.sharkflow.presentation.common.*
import com.example.sharkflow.presentation.screens.task.components.*
import com.example.sharkflow.presentation.screens.task.viewmodel.TaskDetailViewModel
import com.example.sharkflow.utils.DateUtils.formatDateTimeReadable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    navController: NavController,
    boardUuid: String,
    taskUuid: String,
    vm: TaskDetailViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(Unit) { vm.start(boardUuid, taskUuid) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.task?.title ?: "Задача") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primary,
                    titleContentColor = colorScheme.onPrimary,
                    actionIconContentColor = colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                state.task?.let { task ->
                    val status = task.status
                    val priority = task.priority

                    val (statusColor, statusIcon) = when (status) {
                        Status.PENDING -> Color.Gray to Icons.Filled.Schedule
                        Status.IN_PROGRESS -> Color(0xFFFFC107) to Icons.Filled.Timer
                        Status.COMPLETED -> Color(0xFF4CAF50) to Icons.Filled.CheckCircle
                        Status.CANCELLED -> Color(0xFFF44336) to Icons.Filled.Cancel
                    }

                    val (priorityColor, priorityIcon) = when (priority) {
                        Priority.LOW -> Color(0xFFB0BEC5) to Icons.Filled.KeyboardArrowDown
                        Priority.MEDIUM -> Color(0xFFFFB74D) to Icons.Filled.KeyboardArrowUp
                        Priority.HIGH -> Color(0xFFD32F2F) to Icons.Filled.PriorityHigh
                    }

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Описание
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Описание", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    task.description ?: "Нет описания",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        // Статус
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.1f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(statusIcon, contentDescription = "Статус", tint = statusColor)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Статус: ${status.displayName()}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        // Приоритет
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = priorityColor.copy(
                                    alpha = 0.1f
                                )
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    priorityIcon,
                                    contentDescription = "Приоритет",
                                    tint = priorityColor
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Приоритет: ${priority.displayName()}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        // Срок
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Filled.DateRange,
                                    contentDescription = "Срок",
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Срок: ${formatDateTimeReadable(task.dueDate ?: "—")}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        // Создано
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.AddTask, contentDescription = "Создано")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Создано: ${formatDateTimeReadable(task.createdAt)}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        // Обновлено
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.Update, contentDescription = "Обновлено")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Обновлено: ${formatDateTimeReadable(task.updatedAt)}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }


                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AppButton(
                                onClick = { vm.showEditDialog(task) },
                                modifier = Modifier.fillMaxWidth(),
                                icon = (Icons.Filled.Edit),
                                text = "Редактировать"
                            )

                            AppButton(
                                onClick = { vm.showConfirmDelete(task) },
                                icon = (Icons.Filled.Delete),
                                modifier = Modifier.fillMaxWidth(),
                                variant = AppButtonVariant.Outlined,
                                tone = AppButtonTone.Danger,
                                text = "Удалить"
                            )
                        }
                    }

                    vm.editingTask.value?.let { task ->
                        CreateEditTaskDialog(
                            initialTitle = task.title,
                            initialDescription = task.description,
                            initialDueDate = task.dueDate,
                            initialStatus = task.status,
                            initialPriority = task.priority,
                            onDismiss = { vm.dismissEditDialog() },
                            onConfirm = { title, desc, dueDate, status, priority ->
                                vm.updateTask(
                                    task.uuid,
                                    UpdateTaskRequestDto(title, desc, dueDate, status, priority)
                                )
                                vm.dismissEditDialog()
                            }
                        )
                    }

                    vm.showConfirmDelete.value?.let { task ->
                        ConfirmDeleteDialog(
                            title = "Удаление задачи",
                            message = "Удалить \"${task.title}\"?",
                            onConfirm = {
                                vm.deleteTask(task.uuid)
                                vm.dismissDeleteDialog()
                                navController.popBackStack()
                            },
                            onDismiss = { vm.dismissDeleteDialog() }
                        )
                    }
                }
            }
        }
    }
}
