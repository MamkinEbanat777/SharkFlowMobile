package com.example.sharkflow.data.repository.combined

import com.example.sharkflow.data.api.dto.task.*
import com.example.sharkflow.data.local.db.entities.TaskEntity
import com.example.sharkflow.data.mapper.TaskMapper
import com.example.sharkflow.data.repository.local.TaskLocalRepositoryImpl
import com.example.sharkflow.data.repository.remote.TaskRepositoryImpl
import com.example.sharkflow.domain.model.Task
import com.example.sharkflow.domain.repository.TaskRepositoryCombined
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*

class TaskRepositoryCombinedImpl @Inject constructor(
    private val local: TaskLocalRepositoryImpl,
    private val remote: TaskRepositoryImpl
) : TaskRepositoryCombined {

    override fun getTasksFlow(boardUuid: String): Flow<List<Task>> =
        local.getTasksFlow(boardUuid).map { it.map(TaskMapper::fromEntity) }

    override suspend fun refreshTasks(boardUuid: String) {
        val remoteTasks = remote.getTasks(boardUuid).getOrNull() ?: return
        val localEntities = local.getTasksFlow(boardUuid).first()

        val merged = TaskMapper.mergeRemoteWithLocalList(boardUuid, remoteTasks, localEntities)
        local.insertOrUpdateTasks(merged)
    }

    override suspend fun createTask(
        boardUuid: String,
        createDto: CreateTaskRequestDto,
        localUuid: String?
    ): Result<Task> = runCatching {
        val existingLocal: TaskEntity? = localUuid?.let { local.getByLocalUuid(it) }
        val tempEntity = existingLocal?.copy(
            title = createDto.title,
            description = createDto.description,
            status = createDto.status.name,
            priority = createDto.priority.name,
            dueDate = createDto.dueDate,
            isSynced = false,
            isDeleted = false
        ) ?: TaskMapper.toLocalEntityForCreate(boardUuid, createDto, existingUuid = localUuid)

        local.insertOrUpdateTasks(listOf(tempEntity))

        val remoteTask = try {
            remote.createTask(boardUuid, createDto).getOrNull()
        } catch (e: Exception) {
            null
        }

        val serverUuidFromRemote = remoteTask?.serverUuid ?: remoteTask?.uuid
        val mergedEntity =
            TaskMapper.mergeLocalWithRemoteAfterCreate(tempEntity, serverUuidFromRemote)
        local.updateTask(mergedEntity)

        TaskMapper.fromEntity(mergedEntity)
    }

    override suspend fun updateTask(
        boardUuid: String,
        taskUuid: String,
        update: UpdateTaskRequestDto
    ): Result<Task> = runCatching {
        val localEntity = local.getByLocalUuid(taskUuid)
            ?: throw IllegalStateException("Task not found locally for uuid: $taskUuid")

        val serverUuid = localEntity.serverUuid

        if (serverUuid == null) {
            val updated = TaskMapper.mergeEntityWithUpdate(localEntity, update)
                .copy(isSynced = false)
            local.updateTask(updated)
            return@runCatching TaskMapper.fromEntity(updated)
        }

        val remoteResponseAny = try {
            remote.updateTask(boardUuid, serverUuid, update).getOrNull()
        } catch (e: Exception) {
            null
        }

        val mergedEntity = TaskMapper.mergeEntityWithUpdate(localEntity, update, remoteResponseAny)
        local.updateTask(mergedEntity)

        TaskMapper.fromEntity(mergedEntity)
    }

    override suspend fun deleteTask(
        boardUuid: String,
        taskUuid: String,
        hardDelete: Boolean
    ): Result<DeletedTaskInfoDto> = runCatching {
        val localEntity = local.getTasksOnce(boardUuid)
            .find { it.uuid == taskUuid || it.serverUuid == taskUuid }
            ?: return@runCatching DeletedTaskInfoDto(
                title = "Unknown task",
                removedFromBoard = true
            )

        var remoteResult: DeletedTaskInfoDto? = null

        if (localEntity.serverUuid != null) {
            try {
                remoteResult = remote.deleteTask(boardUuid, localEntity.serverUuid).getOrThrow()
            } catch (e: Exception) {
                local.updateTask(localEntity.copy(isDeleted = true, isSynced = false))
            }
        }

        if (hardDelete) {
            local.deleteTask(localEntity)
        } else {
            val isSynced = remoteResult != null
            local.updateTask(localEntity.copy(isDeleted = true, isSynced = isSynced))
        }

        remoteResult ?: DeletedTaskInfoDto(
            title = localEntity.title,
            removedFromBoard = true
        )
    }

    override suspend fun getAllTasks(): List<Task> =
        local.getAllTasks().map(TaskMapper::fromEntity)

    override suspend fun getUnsyncedTasks(): List<Task> =
        local.getUnsyncedTasks().map(TaskMapper::fromEntity)

    override suspend fun getDeletedTasks(): List<Task> =
        local.getDeletedTasks().map(TaskMapper::fromEntity)
}
