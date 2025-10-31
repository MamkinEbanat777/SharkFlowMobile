package com.example.sharkflow.domain.usecase.task

import com.example.sharkflow.data.api.dto.task.UpdateTaskRequestDto
import com.example.sharkflow.domain.model.Task
import com.example.sharkflow.domain.repository.TaskRepositoryCombined
import jakarta.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val repositoryCombined: TaskRepositoryCombined
) {
    suspend operator fun invoke(
        boardUuid: String,
        taskUuid: String,
        update: UpdateTaskRequestDto
    ): Result<Task> = runCatching {
        repositoryCombined.updateTask(boardUuid, taskUuid, update).getOrThrow()
    }
}