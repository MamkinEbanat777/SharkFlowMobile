package com.example.sharkflow.presentation.screens.board

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sharkflow.data.api.dto.task.UpdateTaskRequestDto
import com.example.sharkflow.domain.model.*
import com.example.sharkflow.presentation.screens.board.viewmodel.BoardDetailViewModel
import com.example.sharkflow.presentation.screens.task.components.*
import com.example.sharkflow.presentation.screens.task.viewmodel.TasksViewModel
import com.google.accompanist.swiperefresh.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardDetailScreen(
    navController: NavController,
    boardUuid: String,
    boardVm: BoardDetailViewModel = hiltViewModel(),
    tasksVm: TasksViewModel = hiltViewModel()
) {
    val boardState by boardVm.uiState.collectAsState()
    val tasksState by tasksVm.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        boardVm.startListening(boardUuid)
        tasksVm.start(boardUuid)
        tasksVm.refreshTasks(boardUuid)
    }

    val refreshing = tasksState.isLoading
    val swipeState = rememberSwipeRefreshState(isRefreshing = refreshing)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Surface(
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                color = colorScheme.primary,
                tonalElevation = 4.dp
            ) {
                TopAppBar(
                    title = {
                        Text(
                            boardState.board?.title ?: "Доска",
                            color = colorScheme.onPrimary,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Назад",
                                tint = colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { tasksVm.showCreateDialog() },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Создать")
            }
        }
    ) { padding ->
        SwipeRefresh(
            state = swipeState,
            onRefresh = { tasksVm.refreshTasks(boardUuid) },
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
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when {
                    boardState.isLoading || tasksState.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    tasksState.tasks.isEmpty() -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Пока нет задач",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    else -> {
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(tasksState.tasks, key = { it.uuid }) { task ->
                                TaskRow(
                                    task = task,
                                    onClick = { navController.navigate("task/${boardUuid}/${task.uuid}") },
                                    onEdit = { tasksVm.showEditDialog(task) },
                                    onDelete = { tasksVm.showDeleteDialog(task) }
                                )
                            }
                        }
                    }
                }

                if (tasksVm.showCreateDialog.value) {
                    CreateEditTaskDialog(
                        onDismiss = { tasksVm.dismissCreateDialog() },
                        onConfirm = { title, desc, dueDate, status, priority ->
                            tasksVm.createTask(
                                title = title,
                                description = desc,
                                dueDate = dueDate,
                                status = status ?: TaskStatus.PENDING,
                                priority = priority ?: TaskPriority.MEDIUM
                            )
                            tasksVm.dismissCreateDialog()
                        }

                    )
                }

                tasksVm.editingTask.value?.let { task ->
                    CreateEditTaskDialog(
                        initialTitle = task.title,
                        initialDescription = task.description,
                        initialDueDate = task.dueDate,
                        initialStatus = task.status,
                        initialPriority = task.priority,
                        onDismiss = { tasksVm.dismissEditDialog() },
                        onConfirm = { title, desc, dueDate, status, priority ->
                            tasksVm.updateTask(
                                task.uuid,
                                UpdateTaskRequestDto(
                                    title = title,
                                    description = desc,
                                    dueDate = dueDate,
                                    status = status,
                                    priority = priority
                                )
                            )
                            tasksVm.dismissEditDialog()
                        }
                    )
                }

                tasksVm.showConfirmDelete.value?.let { task ->
                    ConfirmDeleteDialog(
                        title = "Удаление задачи",
                        message = "Удалить \"${task.title}\"?",
                        onConfirm = {
                            tasksVm.deleteTask(task.uuid)
                            tasksVm.dismissDeleteDialog()
                        },
                        onDismiss = { tasksVm.dismissDeleteDialog() }
                    )
                }
            }
        }
    }
}