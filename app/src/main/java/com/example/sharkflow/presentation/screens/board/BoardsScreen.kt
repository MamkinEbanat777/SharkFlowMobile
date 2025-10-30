package com.example.sharkflow.presentation.screens.board

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
import com.example.sharkflow.presentation.common.*
import com.example.sharkflow.presentation.screens.board.components.*
import com.example.sharkflow.presentation.screens.board.viewmodel.BoardsViewModel

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

    val filteredBoards = remember(uiState.boards, searchQuery) {
        val filtered = if (searchQuery.isBlank()) uiState.boards
        else uiState.boards.filter { it.title.contains(searchQuery, ignoreCase = true) }
        filtered.sortedByDescending { it.updatedAt }
    }

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
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                AppField(
                    searchQuery,
                    { searchQuery = it },
                    "Поиск доски",
                    modifier = Modifier.padding(0.dp)
                )
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
            isRefreshing = uiState.isLoading,
            onRefresh = { boardsViewModel.refresh() }
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

                                BoardRow(
                                    board = board,
                                    onClick = { boardsViewModel.openBoard(board.uuid) },
                                    onEdit = { boardsViewModel.onEditBoardClick(board) },
                                    onDelete = { boardsViewModel.onDeleteBoardClick(board) },
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
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
}
