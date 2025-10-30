package com.example.sharkflow.presentation.screens.task

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sharkflow.core.common.DateUtils.formatDateTimeReadable
import com.example.sharkflow.core.presentation.ToastManager
import com.example.sharkflow.core.validators.TaskValidator
import com.example.sharkflow.data.api.dto.task.UpdateTaskRequestDto
import com.example.sharkflow.presentation.common.*
import com.example.sharkflow.presentation.screens.task.components.*
import com.example.sharkflow.presentation.screens.task.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    navController: NavController,
    boardUuid: String,
    taskUuid: String,
    taskDetailViewModel: TaskDetailViewModel,
    tasksViewModel: TasksViewModel
) {
    val state by taskDetailViewModel.uiState.collectAsState()
    val tasksState by tasksViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) { taskDetailViewModel.start(boardUuid, taskUuid) }

    LaunchedEffect(state.message) {
        state.message?.let { msg ->
            if (state.isMessageSuccess) ToastManager.success(context, msg)
            else ToastManager.error(context, msg)
            taskDetailViewModel.clearMessage()
        }
    }

    val cardsOffset = 0

    Scaffold(
        topBar = @Composable {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                TopAppBar(
                    title = { Text(state.task?.title ?: "Задача") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Назад",
                                tint = colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = colorScheme.onPrimary,
                        actionIconContentColor = colorScheme.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },

        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FloatingActionButton(
                    onClick = { taskDetailViewModel.showEditDialog() },
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Редактировать")
                }

                FloatingActionButton(
                    onClick = { taskDetailViewModel.showConfirmDelete() },
                    containerColor = colorScheme.error,
                    contentColor = colorScheme.onSecondary
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Удалить")
                }
            }
        }

    ) { padding ->
        AppSwipeRefresh(
            isRefreshing = state.isLoading,
            onRefresh = { taskDetailViewModel.refreshOnly(boardUuid) }
        )
        {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    state.task?.let { task ->
                        val desc = task.description
                        val descPreview = desc?.let {
                            val max = 60
                            if (it.length > max) it.take(max).trimEnd() + "…" else it
                        } ?: "Нет описания"

                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                StatusBadge(task.status)
                                PriorityBadge(task.priority)
                            }
                            SyncBadge(task.isSynced)

                            // Описание
                            TaskInfoCard(
                                title = "Описание",
                                leadingIcon = Icons.Filled.Description,
                                index = 1,
                                initiallyExpanded = true,
                                inlineValue = descPreview,
                                expandable = true
                            ) {
                                Text(
                                    text = desc ?: "Нет описания",
                                    style = typography.bodyMedium
                                )
                            }

                            state.task!!.dueDate?.let { due ->
                                TaskInfoCard(
                                    title = "Срок",
                                    leadingIcon = Icons.Filled.DateRange,
                                    index = cardsOffset + 2,
                                    expandable = false
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(
                                            text = formatDateTimeReadable(due) ?: "-",
                                            style = typography.bodyMedium,
                                            modifier = Modifier.weight(1f)
                                        )

                                        if (due.isNotEmpty()) {
                                            DeadlineBadge(due)
                                        }
                                    }
                                }
                            }
                            DateBadge("Создано", task.createdAt, Icons.Filled.AddTask)
                            DateBadge("Обновлено", task.updatedAt, Icons.Filled.Update)
                        }

                        if (state.showEditDialog) {
                            val task = state.task
                            CreateEditTaskDialog(
                                initialTitle = task?.title ?: "",
                                initialDescription = task?.description,
                                initialDueDate = task?.dueDate,
                                initialStatus = task?.status,
                                initialPriority = task?.priority,
                                onDismiss = { taskDetailViewModel.dismissEditDialog() },
                                onConfirm = { title, desc, dueDate, status, priority ->
                                    if (TaskValidator.validateTitle(
                                            title,
                                            context,
                                            tasksState.tasks,
                                            task?.uuid
                                        )
                                    ) {
                                        taskDetailViewModel.updateTask(
                                            task?.uuid ?: "",
                                            UpdateTaskRequestDto(
                                                title = title.trim(),
                                                description = desc,
                                                dueDate = dueDate,
                                                status = status,
                                                priority = priority
                                            )
                                        )
                                        taskDetailViewModel.dismissEditDialog()
                                    }
                                }
                            )
                        }

                        if (state.showConfirmDeleteDialog) {
                            val task = state.task
                            ConfirmDeleteDialog(
                                title = "Удаление задачи",
                                message = "Удалить \"${task?.title}\"?",
                                onConfirm = {
                                    taskDetailViewModel.deleteTask(task?.uuid ?: "")
                                    taskDetailViewModel.dismissConfirmDelete()
                                    navController.popBackStack()
                                },
                                onDismiss = { taskDetailViewModel.dismissConfirmDelete() }
                            )
                        }
                    }
                }
            }
        }
    }
}