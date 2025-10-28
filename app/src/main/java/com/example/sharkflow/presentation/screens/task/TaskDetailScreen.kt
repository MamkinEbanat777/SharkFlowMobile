package com.example.sharkflow.presentation.screens.task

import androidx.compose.foundation.*
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
import com.example.sharkflow.core.common.DateUtils.formatDateTimeReadable
import com.example.sharkflow.core.presentation.toUi
import com.example.sharkflow.data.api.dto.task.UpdateTaskRequestDto
import com.example.sharkflow.presentation.common.*
import com.example.sharkflow.presentation.screens.task.components.*
import com.example.sharkflow.presentation.screens.task.viewmodel.TaskDetailViewModel
import com.google.accompanist.swiperefresh.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    navController: NavController,
    boardUuid: String,
    taskUuid: String,
    vm: TaskDetailViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()

    val refreshing = state.isLoading
    val swipeState = rememberSwipeRefreshState(isRefreshing = refreshing)

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
        SwipeRefresh(
            state = swipeState,
            onRefresh = { vm.start(boardUuid, taskUuid) },
            modifier = Modifier
                .fillMaxSize(),
            indicator = { s, _ ->
                SwipeRefreshIndicator(
                    state = s,
                    refreshTriggerDistance = 80.dp,
                    contentColor = colorScheme.primary,
                    backgroundColor = colorScheme.background
                )
            }
        ) {
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

                        val statusUi = remember(task.status) { task.status.toUi() }
                        val priorityUi = remember(task.priority) { task.priority.toUi() }

                        val statusColor = statusUi.color
                        val statusIcon = statusUi.icon

                        val priorityColor = priorityUi.color
                        val priorityIcon = priorityUi.icon

                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
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
                                colors = CardDefaults.cardColors(
                                    containerColor = statusColor.copy(
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
                                        statusIcon,
                                        contentDescription = "Статус",
                                        tint = statusColor
                                    )
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
}