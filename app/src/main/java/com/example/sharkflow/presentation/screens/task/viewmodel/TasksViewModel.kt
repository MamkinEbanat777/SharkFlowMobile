package com.example.sharkflow.presentation.screens.task.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.sharkflow.core.common.DateUtils
import com.example.sharkflow.data.api.dto.task.*
import com.example.sharkflow.domain.model.*
import com.example.sharkflow.domain.usecase.task.*
import com.example.sharkflow.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksFlowUseCase: GetTasksFlowUseCase,
    private val refreshTasksUseCase: RefreshTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
) : BaseViewModel() {
    data class TasksUiState(
        val isLoading: Boolean = false,
        val tasks: List<Task> = emptyList(),
        val showCreateDialog: Boolean = false,
        val editingTask: Task? = null,
        val confirmDeleteTask: Task? = null,
        val message: String? = null,
        val isMessageSuccess: Boolean = true
    )

    private val _uiState = MutableStateFlow(TasksUiState(isLoading = true))
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()
    private val boardUuidFlow = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            boardUuidFlow
                .filterNotNull()
                .flatMapLatest { boardUuid ->
                    getTasksFlowUseCase(boardUuid)
                        .onStart { _uiState.update { it.copy(isLoading = true) } }
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(isLoading = false, message = e.message, isMessageSuccess = false)
                    }
                }
                .collect { list ->
                    _uiState.update { it.copy(isLoading = false, tasks = list) }
                }
        }
    }

    fun setBoardUuid(boardUuid: String) {
        if (boardUuidFlow.value == boardUuid) return
        boardUuidFlow.value = boardUuid
        refreshTasks()
    }

    fun refreshTasks() {
        val boardUuid = boardUuidFlow.value ?: return
        launchResult(
            block = { refreshTasksUseCase(boardUuid); Result.success(Unit) },
            onSuccess = { _uiState.update { it.copy() } },
            onFailure = {
                _uiState.update {
                    it.copy(
                        message = it.message,
                        isMessageSuccess = false
                    )
                }
            }
        )
    }

    fun createTask(
        title: String,
        description: String? = null,
        dueDate: String? = null,
        status: TaskStatus = TaskStatus.PENDING,
        priority: TaskPriority = TaskPriority.MEDIUM
    ) {
        val boardUuid = boardUuidFlow.value ?: return

        val normalizedDue = DateUtils.toServerInstantString(dueDate)
        val dto = CreateTaskRequestDto(title, description, normalizedDue, status, priority)

        launchResult(
            block = { createTaskUseCase(boardUuid, dto, null) },
            onSuccess = {
                _uiState.update {
                    it.copy(
                        showCreateDialog = false,
                        message = "Задача создана",
                        isMessageSuccess = true
                    )
                }
            },
            onFailure = {
                _uiState.update {
                    it.copy(
                        message = "Ошибка создания задачи: ${it.message}",
                        isMessageSuccess = false
                    )
                }
            }
        )
    }

    fun updateTask(localOrUuid: String, update: UpdateTaskRequestDto) {
        val boardUuid = boardUuidFlow.value ?: return

        _uiState.update { state ->
            state.copy(
                tasks = state.tasks.map { t ->
                    if (t.uuid == localOrUuid) {
                        t.copy(
                            title = update.title ?: t.title,
                            description = update.description ?: t.description,
                            dueDate = update.dueDate ?: t.dueDate,
                            status = update.status ?: t.status,
                            priority = update.priority ?: t.priority,
                            updatedAt = Instant.now().toString()
                        )
                    } else t
                }
            )
        }

        val normalizedForSend = DateUtils.toServerInstantString(update.dueDate)
        val dto = update.copy(dueDate = normalizedForSend, updatedAt = Instant.now().toString())

        launchResult(
            block = { updateTaskUseCase(boardUuid, localOrUuid, dto) },
            onSuccess = {
                _uiState.update {
                    it.copy(
                        editingTask = null,
                        message = "Задача обновлена",
                        isMessageSuccess = true
                    )
                }
            },
            onFailure = {
                _uiState.update {
                    it.copy(
                        message = "Ошибка обновления задачи: ${it.message}",
                        isMessageSuccess = false
                    )
                }
            }
        )
    }

    fun deleteTask(taskUuid: String) {
        val boardUuid = boardUuidFlow.value ?: return
        launchResult(
            block = { deleteTaskUseCase(boardUuid, taskUuid) },
            onSuccess = {
                _uiState.update {
                    it.copy(
                        confirmDeleteTask = null,
                        message = "Задача удалена",
                        isMessageSuccess = true
                    )
                }
            },
            onFailure = {
                _uiState.update {
                    it.copy(
                        message = "Ошибка удаления задачи: ${it.message}",
                        isMessageSuccess = false
                    )
                }
            }
        )
    }

    fun showCreateDialog() {
        _uiState.update { it.copy(showCreateDialog = true) }
    }

    fun showEditDialog(task: Task) {
        _uiState.update { it.copy(editingTask = task) }
    }

    fun showDeleteDialog(task: Task) {
        _uiState.update { it.copy(confirmDeleteTask = task) }
    }

    fun dismissCreateDialog() {
        _uiState.update { it.copy(showCreateDialog = false) }
    }

    fun dismissEditDialog() {
        _uiState.update { it.copy(editingTask = null) }
    }

    fun dismissDeleteDialog() {
        _uiState.update { it.copy(confirmDeleteTask = null) }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }
}

