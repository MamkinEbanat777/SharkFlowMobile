package com.example.sharkflow.presentation.screens.board.viewmodel

import androidx.lifecycle.*
import com.example.sharkflow.data.api.dto.board.UpdateBoardRequestDto
import com.example.sharkflow.domain.manager.UserManager
import com.example.sharkflow.domain.model.Board
import com.example.sharkflow.domain.repository.BoardRepositoryCombined
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class BoardDetailUiState(
    val isLoading: Boolean = true,
    val board: Board? = null
)

@HiltViewModel
class BoardDetailViewModel @Inject constructor(
    private val repo: BoardRepositoryCombined,
    private val userManager: UserManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(BoardDetailUiState())
    val uiState: StateFlow<BoardDetailUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun startListening(boardUuid: String) {
        viewModelScope.launch {
            userManager.currentUser
                .filterNotNull()
                .flatMapLatest { user ->
                    repo.getBoardsFlow(userId = 0)
                }
                .map { boards -> boards.find { it.uuid == boardUuid } }
                .collect { b ->
                    _uiState.update { it.copy(isLoading = false, board = b) }
                }
        }
    }

    fun updateBoard(boardUuid: String, update: UpdateBoardRequestDto) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val res = repo.updateBoard(boardUuid, update)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun deleteBoard(boardUuid: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repo.deleteBoard(boardUuid)
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
