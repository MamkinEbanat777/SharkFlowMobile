package com.example.sharkflow.presentation.screens.board

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sharkflow.data.api.dto.board.UpdateBoardRequestDto
import com.example.sharkflow.domain.model.Board
import com.example.sharkflow.presentation.common.AppButton
import com.example.sharkflow.presentation.screens.board.components.*
import com.example.sharkflow.presentation.screens.board.viewmodel.*
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardsScreen(
    navController: NavController,
    vm: BoardsViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        vm.events.collectLatest { event ->
            when (event) {
                is BoardsUiEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.text)
                }

                is BoardsUiEvent.ShowCreateDialog -> {
                }

                is BoardsUiEvent.NavigateToBoard -> {
                    navController.navigate("board/${event.boardUuid}")
                }

                is BoardsUiEvent.NavigateToBoard -> TODO()
                BoardsUiEvent.ShowCreateDialog -> TODO()
                is BoardsUiEvent.ShowMessage -> TODO()
            }
        }
    }

    var showCreateDialog by remember { mutableStateOf(false) }
    var editingBoard by remember { mutableStateOf<Board?>(null) }
    var showConfirmDelete by remember { mutableStateOf<Board?>(null) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Ваши доски") }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.primary,
                    actionIconContentColor = colorScheme.onPrimary
                ), actions = {
                    AppButton(
                        onClick = { vm.refresh() },
                        text = "Обновить",
                        icon = (Icons.Filled.Refresh)
                    )
                })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Создать")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.boards.isEmpty()) {
                Text(
                    text = "Пока нет досок",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    items(uiState.boards, key = { it.uuid }) { board ->
                        BoardRow(
                            board = board,
                            onClick = { vm.openBoard(board.uuid) },
                            onEdit = { editingBoard = board },
                            onDelete = { showConfirmDelete = board }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        if (showCreateDialog) {
            CreateOrEditBoardDialog(
                onDismiss = { showCreateDialog = false },
                onConfirm = { title, color ->
                    vm.createBoard(title, color)
                    showCreateDialog = false
                }
            )
        }

        if (editingBoard != null) {
            CreateOrEditBoardDialog(
                initialTitle = editingBoard!!.title,
                initialColor = editingBoard!!.color ?: "FFFFFF",
                onDismiss = { editingBoard = null },
                onConfirm = { title, color ->
                    vm.updateBoard(
                        editingBoard!!.uuid,
                        UpdateBoardRequestDto(title = title, color = color)
                    )
                    editingBoard = null
                }
            )
        }

        if (showConfirmDelete != null) {
            ConfirmDeleteDialog(
                title = "Удаление доски",
                message = "Удалить доску \"${showConfirmDelete!!.title}\"?",
                onConfirm = {
                    vm.deleteBoard(showConfirmDelete!!.uuid)
                    showConfirmDelete = null
                },
                onDismiss = { showConfirmDelete = null }
            )
        }
    }
}
