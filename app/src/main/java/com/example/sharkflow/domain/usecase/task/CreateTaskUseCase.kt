package com.example.sharkflow.domain.usecase.task

import com.example.sharkflow.data.api.dto.task.CreateTaskRequestDto
import com.example.sharkflow.domain.model.Task
import com.example.sharkflow.domain.repository.TaskRepositoryCombined
import jakarta.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val repositoryCombined: TaskRepositoryCombined
) {
    suspend operator fun invoke(
        boardUuid: String,
        createDto: CreateTaskRequestDto,
        localUuid: String?
    ): Result<Task> = runCatching {
        val task = repositoryCombined.createTask(boardUuid, createDto, localUuid).getOrThrow()
        task
    }
}
