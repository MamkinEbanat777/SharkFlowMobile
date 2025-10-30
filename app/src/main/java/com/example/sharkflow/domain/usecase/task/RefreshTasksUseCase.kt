package com.example.sharkflow.domain.usecase.task

import com.example.sharkflow.domain.repository.TaskRepositoryCombined
import jakarta.inject.Inject

class RefreshTasksUseCase @Inject constructor(
    private val repositoryCombined: TaskRepositoryCombined
) {
    suspend operator fun invoke(boardUuid: String) {
        repositoryCombined.refreshTasks(boardUuid)
    }
}
