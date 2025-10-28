package com.example.sharkflow.presentation.screens.task.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.sharkflow.core.common.DateUtils.formatDateTimeReadable
import com.example.sharkflow.core.system.AppLog
import com.example.sharkflow.data.api.dto.task.UpdateTaskRequestDto
import com.example.sharkflow.domain.model.Task
import com.example.sharkflow.domain.repository.TaskRepositoryCombined
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val repo: TaskRepositoryCombined
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskDetailUiState())
    val uiState = _uiState.asStateFlow()

    val editingTask = mutableStateOf<Task?>(null)
    val showConfirmDelete = mutableStateOf<Task?>(null)

    fun start(boardUuid: String, taskUuid: String) {
        viewModelScope.launch {
            try {
                repo.refreshTasks(boardUuid)
            } catch (_: Exception) {
            }

            repo.getTasksFlow(boardUuid)
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
        viewModelScope.launch {
            _uiState.update { ui ->
                val current = ui.task
                if (current == null) return@update ui.copy(isLoading = true)
                val optimistic = current.copy(
                    title = update.title ?: current.title,
                    description = update.description ?: current.description,
                    dueDate = update.dueDate ?: current.dueDate,
                    status = update.status ?: current.status,
                    priority = update.priority ?: current.priority
                )
                ui.copy(isLoading = true, task = optimistic)
            }

            val boardUuid = _uiState.value.task?.boardUuid
            if (boardUuid == null) {
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            try {
                val res = repo.updateTask(boardUuid, taskUuid, update)
//                try {
//                    repo.refreshTasks(boardUuid)
//                } catch (refreshEx: Exception) {
//                    android.util.Log.e(
//                        "TaskDetailVM",
//                        "refresh after update failed: ${refreshEx.message}"
//                    )
//                }

            } catch (e: Exception) {
                AppLog.e("updateTask failed: ${e.message}", e)
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }


    fun deleteTask(taskUuid: String, hardDelete: Boolean = false) {
        viewModelScope.launch {
            _uiState.value.task?.let { task ->
                repo.deleteTask(task.boardUuid, taskUuid, hardDelete)
            }
        }
    }

    fun showEditDialog(task: Task) {
        editingTask.value = task
    }

    fun dismissEditDialog() {
        editingTask.value = null
    }

    fun showConfirmDelete(task: Task) {
        showConfirmDelete.value = task
    }

    fun dismissDeleteDialog() {
        showConfirmDelete.value = null
    }
}

data class TaskDetailUiState(
    val isLoading: Boolean = true,
    val task: Task? = null,
    val dueDateFormatted: String = "—"
)
