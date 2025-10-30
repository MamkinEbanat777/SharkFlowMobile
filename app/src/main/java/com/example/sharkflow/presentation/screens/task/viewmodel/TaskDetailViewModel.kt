package com.example.sharkflow.presentation.screens.task.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.sharkflow.core.common.DateUtils
import com.example.sharkflow.core.common.DateUtils.formatDateTimeReadable
import com.example.sharkflow.data.api.dto.task.UpdateTaskRequestDto
import com.example.sharkflow.domain.model.Task
import com.example.sharkflow.domain.usecase.task.*
import com.example.sharkflow.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val getTasksFlowUseCase: GetTasksFlowUseCase,
    private val refreshTasksUseCase: RefreshTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
) : BaseViewModel() {
    data class TaskDetailUiState(
        val isLoading: Boolean = true,
        val task: Task? = null,
        val dueDateFormatted: String = "—",
        val showEditDialog: Boolean = false,
        val showConfirmDeleteDialog: Boolean = false,
        val message: String? = null,
        val isMessageSuccess: Boolean = true
    )

    private val _uiState = MutableStateFlow(TaskDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun start(boardUuid: String, taskUuid: String) {
        viewModelScope.launch {
            val initial = getTasksFlowUseCase(boardUuid).firstOrNull()?.find { it.uuid == taskUuid }
            if (initial == null) {
                try {
                    refreshTasksUseCase(boardUuid)
                } catch (_: Exception) {
                }
            }

            getTasksFlowUseCase(boardUuid)
                .map { it.find { t -> t.uuid == taskUuid } }
                .collect { task ->
                    val formatted: String? = formatDateTimeReadable(task?.dueDate)
                    _uiState.update {
                        formatted?.let { dueDateFormatted ->
                            it.copy(
                                isLoading = false,
                                task = task,
                                dueDateFormatted = dueDateFormatted
                            )
                        } ?: it.copy(isLoading = false, task = task)
                    }
                }
        }
    }

    fun updateTask(taskUuid: String, update: UpdateTaskRequestDto) {
        val boardUuid = _uiState.value.task?.boardUuid ?: return

        _uiState.update { state ->
            val current = state.task ?: return@update state
            state.copy(
                task = current.copy(
                    title = update.title ?: current.title,
                    description = update.description ?: current.description,
                    dueDate = update.dueDate ?: current.dueDate,
                    status = update.status ?: current.status,
                    priority = update.priority ?: current.priority,
                    updatedAt = Instant.now().toString()
                )
            )
        }

        val normalizedUpdate = update.copy(
            dueDate = DateUtils.toServerInstantString(update.dueDate),
            updatedAt = Instant.now().toString()
        )

        launchResult(
            block = { updateTaskUseCase(boardUuid, taskUuid, normalizedUpdate) },
            onSuccess = {
                _uiState.update {
                    it.copy(
                        message = "Задача обновлена",
                        isMessageSuccess = true
                    )
                }
            },
            onFailure = {
                _uiState.update {
                    it.copy(
                        message = "Не удалось обновить задачу",
                        isMessageSuccess = false
                    )
                }
            }
        )
    }

    fun deleteTask(taskUuid: String, hardDelete: Boolean = false) {
        val boardUuid = _uiState.value.task?.boardUuid ?: return
        launchResult(
            block = { deleteTaskUseCase(boardUuid, taskUuid, hardDelete); Result.success(Unit) },
            onSuccess = {
                _uiState.update {
                    it.copy(
                        showConfirmDeleteDialog = false,
                        message = "Задача удалена",
                        isMessageSuccess = true
                    )
                }
            },
            onFailure = {
                _uiState.update {
                    it.copy(
                        message = "Не удалось удалить задачу",
                        isMessageSuccess = false
                    )
                }
            }
        )
    }


    fun refreshOnly(boardUuid: String) {
        launchResult(
            block = { refreshTasksUseCase(boardUuid); Result.success(Unit) },
            onFailure = {
                _uiState.update {
                    it.copy(
                        message = "Не удалось обновить задачи",
                        isMessageSuccess = false
                    )
                }
            }
        )
    }

    fun showEditDialog() {
        _uiState.update { it.copy(showEditDialog = true) }
    }

    fun dismissEditDialog() {
        _uiState.update { it.copy(showEditDialog = false) }
    }

    fun showConfirmDelete() {
        _uiState.update { it.copy(showConfirmDeleteDialog = true) }
    }

    fun dismissConfirmDelete() {
        _uiState.update { it.copy(showConfirmDeleteDialog = false) }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }
}