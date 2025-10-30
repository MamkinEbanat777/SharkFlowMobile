package com.example.sharkflow.domain.usecase.task

import com.example.sharkflow.domain.model.Task
import com.example.sharkflow.domain.repository.TaskRepositoryCombined
import jakarta.inject.Inject

class GetUnsyncedTasksUseCase @Inject constructor(
    private val repositoryCombined: TaskRepositoryCombined,
) {
    suspend operator fun invoke(): List<Task> {
        return repositoryCombined.getUnsyncedTasks()
    }
}
