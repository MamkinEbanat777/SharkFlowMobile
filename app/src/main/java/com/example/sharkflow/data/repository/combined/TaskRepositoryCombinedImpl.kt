package com.example.sharkflow.data.repository.combined

import com.example.sharkflow.core.system.AppLog
import com.example.sharkflow.data.api.dto.task.*
import com.example.sharkflow.data.local.db.entities.TaskEntity
import com.example.sharkflow.data.mapper.TaskMapper
import com.example.sharkflow.data.repository.local.*
import com.example.sharkflow.data.repository.remote.TaskRepositoryImpl
import com.example.sharkflow.domain.model.Task
import com.example.sharkflow.domain.repository.TaskRepositoryCombined
import jakarta.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.Instant

class TaskRepositoryCombinedImpl @Inject constructor(
    private val local: TaskLocalRepositoryImpl,
    private val boardLocal: BoardLocalRepositoryImpl,
    private val remote: TaskRepositoryImpl,
) : TaskRepositoryCombined {
    private val ioScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun getTasksFlow(boardUuid: String): Flow<List<Task>> =
        local.getTasksFlow(boardUuid).map { it.map(TaskMapper::fromEntity) }

    override suspend fun refreshTasks(boardUuid: String) {
        val boardServerUuid = boardLocal.getByLocalUuid(boardUuid)?.serverUuid ?: return

        val remoteTasks = runCatching { remote.getTasks(boardServerUuid).getOrNull() }
            .getOrElse {
                AppLog.e(
                    "TaskRepository",
                    "refreshTasks failed for boardServerUuid=$boardServerUuid",
                    it
                )
                return
            } ?: return

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
            isDeleted = false,
            updatedAt = Instant.now().toString()
        ) ?: TaskMapper.toLocalEntityForCreate(boardUuid, createDto, existingUuid = localUuid)

        local.insertOrUpdateTasks(listOf(tempEntity))

        val boardEntity = boardLocal.getByLocalUuid(boardUuid)
        val boardServerUuid = boardEntity?.serverUuid

        val remoteTask = if (boardServerUuid != null) {
            try {
                remote.createTask(boardServerUuid, createDto).getOrNull()
            } catch (e: Exception) {
                AppLog.e(
                    "TaskRepository",
                    "Failed to create task on remote for boardServerUuid=$boardServerUuid",
                    e
                )
                null
            }
        } else null

        val currentFromDb = local.getByLocalUuid(tempEntity.uuid) ?: tempEntity

        val serverUuidFromRemote = remoteTask?.serverUuid
        val now = Instant.now().toString()
        val mergedEntity = if (serverUuidFromRemote != null) {
            currentFromDb.copy(
                serverUuid = serverUuidFromRemote,
                isSynced = true,
                updatedAt = now
            )
        } else {
            currentFromDb.copy(
                isSynced = false,
                updatedAt = now
            )
        }

        local.updateTask(mergedEntity)

        val result = TaskMapper.fromEntity(mergedEntity)
        result
    }

    override suspend fun updateTask(
        boardUuid: String,
        taskUuid: String,
        update: UpdateTaskRequestDto
    ): Result<Task> = runCatching {
        val localEntity = local.getByLocalUuid(taskUuid)
            ?: throw IllegalStateException("Task not found locally for uuid: $taskUuid")

        val updatedLocal = TaskMapper.mergeEntityWithUpdate(localEntity, update)
            .copy(isSynced = false, updatedAt = Instant.now().toString())
        local.updateTask(updatedLocal)

        ioScope.launch {
            val serverUuid = localEntity.serverUuid
            val boardServerUuid = boardLocal.getByLocalUuid(boardUuid)?.serverUuid
            if (serverUuid == null || boardServerUuid == null) return@launch

            val remoteResponseAny = runCatching {
                remote.updateTask(boardServerUuid, serverUuid, update).getOrNull()
            }.getOrNull()

            if (remoteResponseAny != null) {
                val current = local.getByLocalUuid(taskUuid) ?: updatedLocal
                val merged = TaskMapper.mergeEntityWithUpdate(current, update, remoteResponseAny)
                    .copy(isSynced = true, updatedAt = Instant.now().toString())
                local.updateTask(merged)
            } else {
                AppLog.w(
                    "TaskRepository",
                    "Background update failed for task=${taskUuid}, will remain unsynced"
                )
            }
        }

        TaskMapper.fromEntity(updatedLocal)
    }

    override suspend fun deleteTask(
        boardUuid: String,
        taskUuid: String
    ): Result<DeletedTaskInfoDto> = runCatching {
        val task = local.getTasksOnce(boardUuid)
            .find { it.uuid == taskUuid || it.serverUuid == taskUuid }
            ?: return@runCatching DeletedTaskInfoDto("Unknown task", removedFromBoard = true)

        if (task.serverUuid == null) local.deleteTask(task)
        else local.updateTask(task.copy(isDeleted = true, isSynced = false))

        ioScope.launch {
            val serverUuid = task.serverUuid
            val boardServerUuid = boardLocal.getByLocalUuid(boardUuid)?.serverUuid
            if (serverUuid == null || boardServerUuid == null) return@launch

            val remoteResult = runCatching {
                remote.deleteTask(boardServerUuid, serverUuid).getOrNull()
            }.getOrNull()

            if (remoteResult != null) {
                val current = local.getByLocalUuid(task.uuid) ?: task
                local.updateTask(current.copy(isSynced = true))
            } else {
                AppLog.d(
                    "TaskRepository",
                    "Background delete failed for task=${task.uuid}, will remain marked deleted"
                )
            }
        }

        DeletedTaskInfoDto(title = task.title, removedFromBoard = true)
    }

    override suspend fun getAllTasks(): List<Task> =
        local.getAllTasks().map(TaskMapper::fromEntity)

    override suspend fun getUnsyncedTasks(): List<Task> =
        local.getUnsyncedTasks().map(TaskMapper::fromEntity)

    override suspend fun getDeletedTasks(): List<Task> =
        local.getDeletedTasks().map(TaskMapper::fromEntity)
}
