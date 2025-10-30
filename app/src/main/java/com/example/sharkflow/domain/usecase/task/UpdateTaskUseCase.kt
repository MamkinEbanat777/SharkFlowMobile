package com.example.sharkflow.domain.usecase.task

import com.example.sharkflow.data.api.dto.task.UpdateTaskRequestDto
import com.example.sharkflow.data.mapper.TaskMapper
import com.example.sharkflow.data.repository.local.TaskLocalRepositoryImpl
import com.example.sharkflow.domain.model.Task
import com.example.sharkflow.domain.repository.TaskRepositoryCombined
import jakarta.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val repositoryCombined: TaskRepositoryCombined,
    private val local: TaskLocalRepositoryImpl
) {
    suspend operator fun invoke(
        boardUuid: String,
        taskUuid: String,
        update: UpdateTaskRequestDto
    ): Result<Task> = runCatching {
        val localEntity = local.getByLocalUuid(taskUuid)
            ?: throw Exception("Task not found locally")

        val serverUuid = localEntity.serverUuid

        if (serverUuid == null) {
            val updated = TaskMapper.mergeEntityWithUpdate(localEntity, update)
                .copy(isSynced = false)
            local.updateTask(updated)
            return@runCatching TaskMapper.fromEntity(updated)
        }

        val remoteRes = try {
            repositoryCombined.updateTask(boardUuid, serverUuid, update).getOrNull()
        } catch (e: Exception) {
            null
        }

        val mergedEntity = if (remoteRes == null) {
            TaskMapper.mergeEntityWithUpdate(localEntity, update).copy(isSynced = false)
        } else {
            TaskMapper.mergeEntityWithUpdate(
                localEntity,
                update,
                remote = null
            )
            TaskMapper.mergeEntityWithUpdate(localEntity, update).copy(isSynced = true)
        }

        local.updateTask(mergedEntity)
        TaskMapper.fromEntity(mergedEntity)
    }
}
