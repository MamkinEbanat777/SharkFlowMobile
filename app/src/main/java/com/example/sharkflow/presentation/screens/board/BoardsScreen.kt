package com.example.sharkflow.presentation.screens.board

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sharkflow.core.presentation.ToastManager
import com.example.sharkflow.core.validators.BoardValidator
import com.example.sharkflow.data.api.dto.board.UpdateBoardRequestDto
import com.example.sharkflow.domain.model.Board
import com.example.sharkflow.presentation.common.*
import com.example.sharkflow.presentation.screens.board.components.*
import com.example.sharkflow.presentation.screens.board.viewmodel.BoardsViewModel
import com.google.accompanist.swiperefresh.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardsScreen(
    navController: NavController,
    boardsViewModel: BoardsViewModel
) {
    val uiState by boardsViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var favoritesOnly by remember { mutableStateOf(false) }

    val filteredBoards = remember(uiState.boards, searchQuery, favoritesOnly) {
        val byQuery = if (searchQuery.isBlank()) uiState.boards
        else uiState.boards.filter { it.title.contains(searchQuery, ignoreCase = true) }

        val byFavorite = if (favoritesOnly) byQuery.filter { it.isFavorite } else byQuery

        byFavorite.sortedWith(
            compareByDescending<Board> { it.isPinned }
                .thenByDescending { it.updatedAt ?: "" }
        )
    }

    val swipeState = rememberSwipeRefreshState(isRefreshing = uiState.isLoading)
    val boardVisibilityMap = remember { mutableStateMapOf<String, Boolean>() }

    // Навигация
    LaunchedEffect(uiState.navigateToBoardUuid) {
        uiState.navigateToBoardUuid?.let { uuid ->
            navController.navigate("board/$uuid")
            boardsViewModel.clearNavigation()
        }
    }

    // Сообщения
    LaunchedEffect(uiState.message) {
        uiState.message?.let { msg ->
            if (uiState.isMessageSuccess) ToastManager.success(context, msg)
            else ToastManager.error(context, msg)
            boardsViewModel.clearMessage()
        }
    }

    LaunchedEffect(Unit) {
        boardsViewModel.refreshOnce()
    }

    // UI
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 0.dp, start = 8.dp, end = 8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        color = colorScheme.primary,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Мои доски",
                                style = MaterialTheme.typography.titleLarge,
                                color = colorScheme.onPrimary
                            )
                        }
                    }
                    AppSearchField(
                        title = "Поиск доски",
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        modifier = Modifier.padding(0.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = favoritesOnly,
                            onCheckedChange = { favoritesOnly = it },
                            colors = CheckboxDefaults.colors(
                                checkmarkColor = colorScheme.onPrimary,
                                checkedColor = colorScheme.primary
                            )
                        )
                        Text(
                            text = "Только избранные",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorScheme.onSurface
                        )
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { boardsViewModel.onAddBoardClick() },
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Создать")
                }
            }
        ) { innerPadding ->
            AppSwipeRefresh(
                state = swipeState,
                onRefresh = { boardsViewModel.refresh() },
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            if (filteredBoards.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillParentMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Пока нет досок",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            } else {
                                items(filteredBoards, key = { it.uuid }) { board ->
                                    val visible = boardVisibilityMap.getOrPut(board.uuid) { false }

                                    LaunchedEffect(board.uuid) {
                                        if (!visible) {
                                            boardVisibilityMap[board.uuid] = true
                                        }
                                    }

                                    AnimatedVisibility(
                                        visible = boardVisibilityMap[board.uuid] == true,
                                        enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                                            initialOffsetY = { it / 2 },
                                            animationSpec = tween(300)
                                        ),
                                        exit = fadeOut(animationSpec = tween(200)) + slideOutVertically(
                                            targetOffsetY = { it / 2 },
                                            animationSpec = tween(200)
                                        )
                                    ) {
                                        BoardRow(
                                            board = board,
                                            onClick = { boardsViewModel.openBoard(board.uuid) },
                                            onEdit = { boardsViewModel.onEditBoardClick(board) },
                                            onDelete = { boardsViewModel.onDeleteBoardClick(board) },
                                            boardsViewModel = boardsViewModel
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                            }
                            item { Spacer(modifier = Modifier.height(76.dp)) }
                        }
                    }
                }

                // Создание доски
                if (uiState.showCreateDialog) {
                    CreateOrEditBoardDialog(
                        onDismiss = { boardsViewModel.dismissDialogs() },
                        onConfirm = { title, color ->
                            if (BoardValidator.validateTitle(title, context, uiState.boards)) {
                                boardsViewModel.createBoard(title.trim(), color)
                                boardsViewModel.dismissDialogs()
                            }
                        }
                    )
                }

                // Редактирование доски
                uiState.editingBoard?.let { board ->
                    CreateOrEditBoardDialog(
                        initialTitle = board.title,
                        initialColor = board.color ?: "FFFFFF",
                        onDismiss = { boardsViewModel.dismissDialogs() },
                        onConfirm = { title, color ->
                            if (BoardValidator.validateTitle(
                                    title,
                                    context,
                                    uiState.boards,
                                    board.uuid
                                )
                            ) {
                                boardsViewModel.updateBoard(
                                    board.uuid,
                                    UpdateBoardRequestDto(title.trim(), color)
                                )
                                boardsViewModel.dismissDialogs()
                            }
                        }
                    )
                }

                // Подтверждение удаления
                uiState.confirmDeleteBoard?.let { board ->
                    ConfirmDeleteDialog(
                        title = "Удаление доски",
                        message = "Удалить доску \"${board.title}\"?",
                        onConfirm = {
                            boardsViewModel.deleteBoard(board.uuid)
                        },
                        onDismiss = { boardsViewModel.dismissDialogs() }
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