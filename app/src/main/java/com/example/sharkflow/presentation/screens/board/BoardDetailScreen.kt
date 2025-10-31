package com.example.sharkflow.presentation.screens.board

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sharkflow.core.presentation.ToastManager
import com.example.sharkflow.core.validators.TaskValidator
import com.example.sharkflow.data.api.dto.task.UpdateTaskRequestDto
import com.example.sharkflow.domain.model.*
import com.example.sharkflow.presentation.common.*
import com.example.sharkflow.presentation.screens.board.components.FavoriteBadge
import com.example.sharkflow.presentation.screens.board.components.PinnedBadge
import com.example.sharkflow.presentation.screens.board.viewmodel.BoardsViewModel
import com.example.sharkflow.presentation.screens.task.components.*
import com.example.sharkflow.presentation.screens.task.viewmodel.TasksViewModel
import com.google.accompanist.swiperefresh.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardDetailScreen(
    navController: NavController,
    boardUuid: String,
    boardsViewModel: BoardsViewModel,
    tasksViewModel: TasksViewModel
) {
    val boardsUiState by boardsViewModel.uiState.collectAsState()
    val tasksUiState by tasksViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val board = remember(boardsUiState.boards, boardUuid) {
        boardsUiState.boards.find { it.uuid == boardUuid }
    }

    var searchQuery by remember { mutableStateOf("") }
    val filteredTasks = remember(tasksUiState.tasks, searchQuery) {
        val filtered = if (searchQuery.isBlank()) tasksUiState.tasks
        else tasksUiState.tasks.filter { it.title.contains(searchQuery, ignoreCase = true) }
        filtered.sortedByDescending { it.updatedAt }
    }

    val swipeState = rememberSwipeRefreshState(isRefreshing = tasksUiState.isLoading)
    val boardVisibilityMap = remember { mutableStateMapOf<String, Boolean>() }

    LaunchedEffect(tasksUiState.message) {
        tasksUiState.message?.let { msg ->
            if (tasksUiState.isMessageSuccess) ToastManager.success(context, msg)
            else ToastManager.error(context, msg)
            tasksViewModel.clearMessage()
        }
    }

    LaunchedEffect(boardUuid) {
        tasksViewModel.setBoardUuid(boardUuid)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        color = colorScheme.primary,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TopAppBar(
                            title = {
                                Text(
                                    text = board?.title ?: "Доска",
                                    color = colorScheme.onPrimary,
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Назад",
                                        tint = colorScheme.onPrimary,
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent
                            )
                        )
                    }

                    AppSearchField(
                        title = "Поиск задачи",
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        modifier = Modifier.padding(0.dp)
                    )

                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FavoriteBadge(board?.isFavorite ?: false)
                            PinnedBadge(board?.isPinned ?: false)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        SyncBadge((board?.isSynced ?: "") as Boolean)
                    }
                }
            },

            floatingActionButton = {
                FloatingActionButton(
                    onClick = { tasksViewModel.showCreateDialog() },
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Создать")
                }
            }
        ) { padding ->
            AppSwipeRefresh(
                state = swipeState,
                onRefresh = { tasksViewModel.refreshTasks() },
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    val showLoading =
                        tasksUiState.isLoading || (boardsUiState.isLoading && board == null)

                    if (showLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else {
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            if (tasksUiState.tasks.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier.fillParentMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Пока нет задач",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            } else {
                                items(filteredTasks, key = { it.uuid }) { task ->
                                    val visible =
                                        boardVisibilityMap.getOrPut(board?.uuid ?: "") { false }

                                    LaunchedEffect(board?.uuid) {
                                        if (!visible) {
                                            boardVisibilityMap[board?.uuid ?: ""] = true
                                        }
                                    }

                                    AnimatedVisibility(
                                        visible = boardVisibilityMap[board?.uuid ?: ""] == true,
                                        enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                                            initialOffsetY = { it / 2 },
                                            animationSpec = tween(300)
                                        ),
                                        exit = fadeOut(animationSpec = tween(200)) + slideOutVertically(
                                            targetOffsetY = { it / 2 },
                                            animationSpec = tween(200)
                                        )
                                    ) {
                                        TaskRow(
                                            task = task,
                                            onClick = { navController.navigate("task/${boardUuid}/${task.uuid}") },
                                            onEdit = { tasksViewModel.showEditDialog(task) },
                                            onDelete = { tasksViewModel.showDeleteDialog(task) }
                                        )
                                    }
                                }
                            }
                            item { Spacer(modifier = Modifier.height(80.dp)) }
                        }
                    }
                }

                if (tasksUiState.showCreateDialog) {
                    CreateEditTaskDialog(
                        onDismiss = { tasksViewModel.dismissCreateDialog() },
                        onConfirm = { title, desc, dueDate, status, priority ->
                            if (TaskValidator.validateTitle(title, context, tasksUiState.tasks)) {
                                tasksViewModel.createTask(
                                    title = title.trim(),
                                    description = desc,
                                    dueDate = dueDate,
                                    status = status ?: TaskStatus.PENDING,
                                    priority = priority ?: TaskPriority.MEDIUM
                                )
                                tasksViewModel.dismissCreateDialog()
                            }
                        }
                    )
                }

                tasksUiState.editingTask?.let { task ->
                    CreateEditTaskDialog(
                        initialTitle = task.title,
                        initialDescription = task.description,
                        initialDueDate = task.dueDate,
                        initialStatus = task.status,
                        initialPriority = task.priority,
                        onDismiss = { tasksViewModel.dismissEditDialog() },
                        onConfirm = { title, desc, dueDate, status, priority ->
                            if (TaskValidator.validateTitle(
                                    title,
                                    context,
                                    tasksUiState.tasks,
                                    task.uuid
                                )
                            ) {
                                tasksViewModel.updateTask(
                                    task.uuid,
                                    UpdateTaskRequestDto(
                                        title = title.trim(),
                                        description = desc,
                                        dueDate = dueDate,
                                        status = status,
                                        priority = priority
                                    )
                                )
                                tasksViewModel.dismissEditDialog()
                            }
                        }
                    )
                }

                tasksUiState.confirmDeleteTask?.let { task ->
                    ConfirmDeleteDialog(
                        title = "Удаление задачи",
                        message = "Удалить \"${task.title}\"?",
                        onConfirm = {
                            tasksViewModel.deleteTask(task.uuid)
                            tasksViewModel.dismissDeleteDialog()
                        },
                        onDismiss = { tasksViewModel.dismissDeleteDialog() }
                    )
                }
            }
        }
        SwipeRefreshIndicator(
            state = swipeState,
            refreshTriggerDistance = 80.dp,
            contentColor = colorScheme.primary,
            backgroundColor = colorScheme.background,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(99f)
                .padding(top = 8.dp)
        )
    }
}