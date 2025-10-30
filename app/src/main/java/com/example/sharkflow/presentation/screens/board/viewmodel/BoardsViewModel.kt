package com.example.sharkflow.presentation.screens.board.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.sharkflow.core.system.AppLog
import com.example.sharkflow.data.api.dto.board.UpdateBoardRequestDto
import com.example.sharkflow.domain.manager.UserManager
import com.example.sharkflow.domain.model.Board
import com.example.sharkflow.domain.usecase.board.*
import com.example.sharkflow.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BoardsViewModel @Inject constructor(
    private val refreshBoardsUseCase: RefreshBoardsUseCase,
    private val getBoardsFlowUseCase: GetBoardsFlowUseCase,
    private val createBoardUseCase: CreateBoardUseCase,
    private val updateBoardUseCase: UpdateBoardUseCase,
    private val deleteBoardUseCase: DeleteBoardUseCase,
    private val userManager: UserManager
) : BaseViewModel() {
    data class BoardsUiState(
        val isLoading: Boolean = false,
        val boards: List<Board> = emptyList(),
        val showCreateDialog: Boolean = false,
        val editingBoard: Board? = null,
        val confirmDeleteBoard: Board? = null,
        val message: String? = null,
        val isMessageSuccess: Boolean = true,
        val navigateToBoardUuid: String? = null
    )

    private val _uiState = MutableStateFlow(BoardsUiState(isLoading = true))
    val uiState: StateFlow<BoardsUiState> = _uiState.asStateFlow()

    private val userUuidFlow: Flow<String?> by lazy { userManager.currentUser.map { it?.uuid } }

    private val hasRefreshed = AtomicBoolean(false)

    init {
        viewModelScope.launch {
            userUuidFlow
                .filterNotNull()
                .flatMapLatest {
                    getBoardsFlowUseCase()
                }
                .collect { boards ->
                    _uiState.update { it.copy(isLoading = false, boards = boards) }
                }
        }
    }

    fun refreshOnce() {
        if (!hasRefreshed.compareAndSet(false, true)) return

        viewModelScope.launch {
            try {
                userUuidFlow.filterNotNull().first()

                launchResult(
                    block = {
                        refreshBoardsUseCase()
                        Result.success(Unit)
                    },
                    onFailure = { throwable ->
                        _uiState.update {
                            it.copy(
                                message = throwable?.message ?: "Ошибка обновления досок",
                                isMessageSuccess = false
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                AppLog.e("refreshOnce: failed to wait userUuid: ${e.message}", e)
            }
        }
    }

    fun refresh() {
        launchResult(
            block = {
                refreshBoardsUseCase()
                Result.success(Unit)
            },
            onFailure = { throwable ->
                _uiState.update {
                    it.copy(
                        message = throwable?.message ?: "Ошибка обновления досок",
                        isMessageSuccess = false
                    )
                }
            }
        )
    }


    fun createBoard(title: String, colorHex: String) {
        launchResult(
            block = { createBoardUseCase(title, colorHex) },
            onSuccess = {
                _uiState.update {
                    it.copy(
                        showCreateDialog = false,
                        message = "Доска успешно создана",
                        isMessageSuccess = true
                    )
                }
            },
            onFailure = {
                _uiState.update {
                    it.copy(
                        message = "Ошибка создания доски: ${it.message}",
                        isMessageSuccess = false
                    )
                }
            }
        )
    }

    fun updateBoard(boardUuid: String, update: UpdateBoardRequestDto) {
        launchResult(
            block = { updateBoardUseCase(boardUuid, update) },
            onSuccess = {
                _uiState.update {
                    it.copy(
                        message = "Доска успешно обновлена",
                        isMessageSuccess = true,
                        editingBoard = null
                    )
                }
            },
            onFailure = {
                _uiState.update {
                    it.copy(
                        message = "Ошибка обновления доски: ${it.message}",
                        isMessageSuccess = false
                    )
                }
            }
        )
    }

    fun deleteBoard(boardUuid: String) {
        launchResult(
            block = { deleteBoardUseCase(boardUuid) },
            onSuccess = {
                _uiState.update {
                    it.copy(
                        message = "Доска успешно удалена",
                        isMessageSuccess = true,
                        confirmDeleteBoard = null
                    )
                }
            },
            onFailure = {
                _uiState.update {
                    it.copy(
                        message = "Ошибка удаления доски: ${it.message}",
                        isMessageSuccess = false
                    )
                }
            }
        )
    }

    fun openBoard(boardUuid: String) {
        _uiState.update { it.copy(navigateToBoardUuid = boardUuid) }
    }

    fun onAddBoardClick() {
        _uiState.update { it.copy(showCreateDialog = true) }
    }

    fun onEditBoardClick(board: Board) {
        _uiState.update { it.copy(editingBoard = board) }
    }

    fun onDeleteBoardClick(board: Board) {
        _uiState.update { it.copy(confirmDeleteBoard = board) }
    }

    fun dismissDialogs() {
        _uiState.update {
            it.copy(
                showCreateDialog = false,
                editingBoard = null,
                confirmDeleteBoard = null
            )
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }

    fun clearNavigation() {
        _uiState.update { it.copy(navigateToBoardUuid = null) }
    }
}
