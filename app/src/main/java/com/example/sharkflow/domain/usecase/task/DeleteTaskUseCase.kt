package com.example.sharkflow.domain.usecase.task

import com.example.sharkflow.data.api.dto.task.DeletedTaskInfoDto
import com.example.sharkflow.domain.repository.TaskRepositoryCombined
import jakarta.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repo: TaskRepositoryCombined
) {
    suspend operator fun invoke(
        boardUuid: String,
        taskUuid: String,
        hardDelete: Boolean
    ): Result<DeletedTaskInfoDto> {
        return repo.deleteTask(boardUuid, taskUuid, hardDelete)
    }
}
