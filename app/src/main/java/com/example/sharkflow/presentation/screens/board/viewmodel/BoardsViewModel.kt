package com.example.sharkflow.presentation.screens.board.viewmodel

import androidx.lifecycle.*
import com.example.sharkflow.data.api.dto.board.UpdateBoardRequestDto
import com.example.sharkflow.domain.manager.UserManager
import com.example.sharkflow.domain.model.Board
import com.example.sharkflow.domain.repository.BoardRepositoryCombined
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class BoardsUiEvent {
    data class ShowMessage(val text: String) : BoardsUiEvent()
    object ShowCreateDialog : BoardsUiEvent()
    data class NavigateToBoard(val boardUuid: String) : BoardsUiEvent()
}

data class BoardsUiState(
    val isLoading: Boolean = false,
    val boards: List<Board> = emptyList()
)

@HiltViewModel
class BoardsViewModel @Inject constructor(
    private val repo: BoardRepositoryCombined,
    private val userManager: UserManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(BoardsUiState(isLoading = true))
    val uiState: StateFlow<BoardsUiState> = _uiState.asStateFlow()

    private val _events = Channel<BoardsUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val userUuidFlow: Flow<String?> = userManager.currentUser.map { it?.uuid }

    init {
        viewModelScope.launch {
            userUuidFlow.collectLatest { uuid ->
                if (uuid == null) {
                    _uiState.update { it.copy(isLoading = false, boards = emptyList()) }
                } else {
                    repo.getBoardsFlow(userId = 0)
                    repo.getBoardsFlow(userId = 0)
                        .catch { e ->
                            _uiState.update { it.copy(isLoading = false) }
                            _events.send(BoardsUiEvent.ShowMessage("Ошибка загрузки досок: ${e.message}"))
                        }
                        .collect { boards ->
                            _uiState.update { it.copy(isLoading = false, boards = boards) }
                        }
                }
            }
        }
    }

    fun onCreateClicked() {
        viewModelScope.launch { _events.send(BoardsUiEvent.ShowCreateDialog) }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val userUuid = userManager.currentUser.value?.uuid ?: run {
                _events.send(BoardsUiEvent.ShowMessage("Пользователь не залогинен"))
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }
            repo.refreshBoards(userId = 0)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun createBoard(title: String, colorHex: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val res = repo.createBoard(title, colorHex)
            res.onSuccess {
                _events.send(BoardsUiEvent.ShowMessage("Доска успешно создана"))
            }.onFailure {
                _events.send(BoardsUiEvent.ShowMessage("Ошибка создания доски: ${it.message}"))
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun updateBoard(boardUuid: String, update: UpdateBoardRequestDto) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val res = repo.updateBoard(boardUuid, update)
            res.onSuccess {
                _events.send(BoardsUiEvent.ShowMessage("Доска успешно обновлена"))
            }.onFailure {
                _events.send(BoardsUiEvent.ShowMessage("Ошибка обновления доски: ${it.message}"))
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun deleteBoard(boardUuid: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val res = repo.deleteBoard(boardUuid)
            res.onSuccess {
                _events.send(BoardsUiEvent.ShowMessage("Доска успешно удалена"))
            }.onFailure {
                _events.send(BoardsUiEvent.ShowMessage("Ошибка удаления доски: ${it.message}"))
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun openBoard(boardUuid: String) {
        viewModelScope.launch { _events.send(BoardsUiEvent.NavigateToBoard(boardUuid)) }
    }

}
