package com.example.sharkflow.domain.usecase.task

import com.example.sharkflow.data.api.dto.task.UpdateTaskRequestDto
import com.example.sharkflow.data.mapper.TaskMapper
import com.example.sharkflow.data.repository.local.*
import com.example.sharkflow.domain.model.Task
import com.example.sharkflow.domain.repository.TaskRepository
import jakarta.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val local: TaskLocalRepositoryImpl,
    private val boardLocalRepositoryImpl: BoardLocalRepositoryImpl,
    private val remote: TaskRepository
) {
    suspend operator fun invoke(
        boardUuid: String,
        taskUuid: String,
        update: UpdateTaskRequestDto
    ): Result<Task> = runCatching {
        // Берём актуальную локальную задачу
        val localEntity = local.getByLocalUuid(taskUuid)
            ?: throw Exception("Task not found locally")

        val boardServerUuid = boardLocalRepositoryImpl.getByLocalUuid(boardUuid)?.serverUuid

        val mergedEntity = if (localEntity.serverUuid == null || boardServerUuid == null) {
            TaskMapper.mergeEntityWithUpdate(localEntity, update)
                .copy(isSynced = false)
        } else {
            val remoteRes = try {
                remote.updateTask(boardServerUuid, localEntity.serverUuid, update).getOrNull()
            } catch (e: Exception) {
                null
            }

            val current = local.getByLocalUuid(taskUuid) ?: localEntity
            if (remoteRes == null) {
                TaskMapper.mergeEntityWithUpdate(current, update)
                    .copy(isSynced = false)
            } else {
                TaskMapper.mergeEntityWithUpdate(current, update, remoteRes)
                    .copy(isSynced = true)
            }
        }

        local.updateTask(mergedEntity)
        TaskMapper.fromEntity(mergedEntity)
    }
}
