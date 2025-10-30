package com.example.sharkflow.domain.usecase.task

import com.example.sharkflow.data.mapper.TaskMapper
import com.example.sharkflow.data.repository.local.TaskLocalRepositoryImpl
import com.example.sharkflow.domain.model.Task
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*

class GetTasksFlowUseCase @Inject constructor(
    private val local: TaskLocalRepositoryImpl,
) {
    operator fun invoke(boardUuid: String): Flow<List<Task>> =
        local.getTasksFlow(boardUuid).map { it.map(TaskMapper::fromEntity) }
}
