package com.example.sharkflow.presentation.screens.task.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.example.sharkflow.data.api.dto.task.*
import com.example.sharkflow.domain.manager.UserManager
import com.example.sharkflow.domain.model.Task
import com.example.sharkflow.domain.repository.TaskRepositoryCombined
import com.example.sharkflow.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val repo: TaskRepositoryCombined,
    private val userManager: UserManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState(isLoading = true))
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    private val _events = Channel<TasksUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var currentBoardUuid: String? = null
    private var tasksJob: Job? = null

    private val _showCreateDialog = mutableStateOf(false)
    val showCreateDialog: State<Boolean> = _showCreateDialog

    private val _editingTask = mutableStateOf<Task?>(null)
    val editingTask: State<Task?> = _editingTask

    private val _showConfirmDelete = mutableStateOf<Task?>(null)
    val showConfirmDelete: State<Task?> = _showConfirmDelete
    fun start(boardUuid: String) {
        tasksJob?.cancel()
        currentBoardUuid = boardUuid
        tasksJob = viewModelScope.launch {
            repo.getTasksFlow(boardUuid)
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { e -> _uiState.update { it.copy(isLoading = false); it } }
                .collect { list ->
                    _uiState.update { it.copy(isLoading = false, tasks = list) }
                }
        }
    }

    fun createTask(
        title: String,
        description: String?,
        dueDate: String? = null,
        status: Status = Status.PENDING,
        priority: Priority = Priority.MEDIUM
    ) {
        val board = currentBoardUuid ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val normalizedDue = DateUtils.toServerInstantString(dueDate)

            val createDto = CreateTaskRequestDto(
                title = title,
                description = description,
                dueDate = normalizedDue,
                status = status,
                priority = priority
            )

            val res = repo.createTask(board, createDto)
            res.onSuccess {
                _events.send(TasksUiEvent.ShowMessage("Task created"))
                try {
                    repo.refreshTasks(board)
                } catch (_: Exception) {
                }
            }.onFailure { _events.send(TasksUiEvent.ShowMessage("Create failed: ${it.message}")) }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun updateTask(taskUuid: String, update: UpdateTaskRequestDto) {
        val board = currentBoardUuid ?: return

        _uiState.update { state ->
            state.copy(
                tasks = state.tasks.map { t ->
                    if (t.uuid == taskUuid) t.copy(
                        title = update.title ?: t.title,
                        description = update.description ?: t.description,
                        dueDate = update.dueDate ?: t.dueDate,
                        status = update.status ?: t.status,
                        priority = update.priority ?: t.priority
                    ) else t
                }
            )
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val normalizedForSend = DateUtils.toServerInstantString(update.dueDate)
            val updateToSend = UpdateTaskRequestDto(
                title = update.title,
                description = update.description,
                dueDate = normalizedForSend,
                status = update.status,
                priority = update.priority
            )

            val res = repo.updateTask(board, taskUuid, updateToSend)
            res.onSuccess {
                _events.send(TasksUiEvent.ShowMessage("Task updated"))
                try {
                    repo.refreshTasks(board)
                } catch (e: Exception) {
                    Log.e("TasksViewModel", "refresh after update failed: ${e.message}")
                }
            }.onFailure {
                _events.send(TasksUiEvent.ShowMessage("Update failed: ${it.message}"))
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }


    fun deleteTask(taskUuid: String, hardDelete: Boolean = false) {
        val board = currentBoardUuid ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val res = repo.deleteTask(board, taskUuid, hardDelete)
            res.onSuccess { _events.send(TasksUiEvent.ShowMessage("Task deleted")) }
            res.onFailure { _events.send(TasksUiEvent.ShowMessage("Delete failed: ${it.message}")) }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun sendEvent(ev: TasksUiEvent) {
        viewModelScope.launch {
            _events.send(ev)
        }
    }


    fun showCreateDialog() {
        _showCreateDialog.value = true
    }

    fun showEditDialog(task: Task) {
        _editingTask.value = task
    }

    fun showDeleteDialog(task: Task) {
        _showConfirmDelete.value = task
    }

    fun dismissCreateDialog() {
        _showCreateDialog.value = false
    }

    fun dismissEditDialog() {
        _editingTask.value = null
    }

    fun dismissDeleteDialog() {
        _showConfirmDelete.value = null
    }

}

data class TasksUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList()
)

sealed class TasksUiEvent {
    data class ShowMessage(val text: String) : TasksUiEvent()
    data class OpenTask(val taskUuid: String) : TasksUiEvent()
}

