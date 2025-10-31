package com.example.sharkflow.data.repository.combined

import com.example.sharkflow.core.system.AppLog
import com.example.sharkflow.data.api.dto.task.*
import com.example.sharkflow.data.local.db.entities.TaskEntity
import com.example.sharkflow.data.mapper.TaskMapper
import com.example.sharkflow.data.repository.local.*
import com.example.sharkflow.data.repository.remote.TaskRepositoryImpl
import com.example.sharkflow.domain.model.Task
import com.example.sharkflow.domain.repository.*
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import java.time.Instant

class TaskRepositoryCombinedImpl @Inject constructor(
    private val local: TaskLocalRepositoryImpl,
    private val boardLocal: BoardLocalRepositoryImpl,
    private val remote: TaskRepositoryImpl,
    private val boardRepositoryCombined: BoardRepositoryCombined
) : TaskRepositoryCombined {

    override fun getTasksFlow(boardUuid: String): Flow<List<Task>> =
        local.getTasksFlow(boardUuid).map { it.map(TaskMapper::fromEntity) }

    override suspend fun refreshTasks(boardUuid: String) {
        val boardServerUuid = boardLocal.getByLocalUuid(boardUuid)?.serverUuid
        if (boardServerUuid == null) {
            AppLog.d(
                "TaskRepository",
                "refreshTasks: board not synced (local=$boardUuid) -> skipping remote refresh"
            )
            return
        }

        val remoteTasks = try {
            remote.getTasks(boardServerUuid).getOrNull()
        } catch (e: Exception) {
            AppLog.e(
                "TaskRepository",
                "refreshTasks: failed to fetch remote tasks for boardServerUuid=$boardServerUuid",
                e
            )
            null
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
        AppLog.d(
            "TaskRepository",
            "createTask START: localUuid=$localUuid, boardUuid=$boardUuid, title=${createDto.title}"
        )

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

        AppLog.d("TaskRepository", "Temp local entity prepared: $tempEntity")
        local.insertOrUpdateTasks(listOf(tempEntity))
        AppLog.d("TaskRepository", "Inserted/updated temp local entity into DB")

        val boardEntity = boardLocal.getByLocalUuid(boardUuid)
        val boardServerUuid = boardEntity?.serverUuid
        AppLog.d("TaskRepository", "Board localUuid=$boardUuid serverUuid=$boardServerUuid")

        val remoteTask = if (boardServerUuid != null) {
            try {
                AppLog.d(
                    "TaskRepository",
                    "Sending create request to remote for boardServerUuid=$boardServerUuid"
                )
                remote.createTask(boardServerUuid, createDto).getOrNull().also {
                    AppLog.d("TaskRepository", "Remote create result: $it")
                }
            } catch (e: Exception) {
                AppLog.e(
                    "TaskRepository",
                    "Failed to create task on remote for boardServerUuid=$boardServerUuid",
                    e
                )
                null
            }
        } else {
            AppLog.d("TaskRepository", "Board not synced yet — skipping remote create")
            null
        }

        val currentFromDb = local.getByLocalUuid(tempEntity.uuid) ?: tempEntity
        AppLog.d("TaskRepository", "Current from DB before merge: $currentFromDb")

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

        AppLog.d("TaskRepository", "Merged entity to write to DB: $mergedEntity")
        local.updateTask(mergedEntity)
        AppLog.d("TaskRepository", "Local entity updated after merge")

        val after = local.getByLocalUuid(mergedEntity.uuid)
        AppLog.d("TaskRepository", "Current from DB after update: $after")

        val result = TaskMapper.fromEntity(mergedEntity)
        AppLog.d("TaskRepository", "createTask END: result=$result")
        result
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

        val boardServerUuid = boardLocal.getByLocalUuid(boardUuid)?.serverUuid
        if (boardServerUuid == null) {
            val updated =
                TaskMapper.mergeEntityWithUpdate(localEntity, update).copy(isSynced = false)
            local.updateTask(updated)
            return@runCatching TaskMapper.fromEntity(updated)
        }

        val remoteResponseAny = try {
            remote.updateTask(boardServerUuid, serverUuid, update).getOrNull()
        } catch (e: Exception) {
            AppLog.e(
                "TaskRepository",
                "updateTask: remote update failed for boardServerUuid=$boardServerUuid taskServerUuid=$serverUuid",
                e
            )
            null
        }

        val mergedEntity = if (remoteResponseAny == null) {
            TaskMapper.mergeEntityWithUpdate(localEntity, update).copy(isSynced = false)
        } else {
            TaskMapper.mergeEntityWithUpdate(localEntity, update, remoteResponseAny)
                .copy(isSynced = true)
        }

        local.updateTask(mergedEntity)
        TaskMapper.fromEntity(mergedEntity)
    }

    override suspend fun deleteTask(
        boardUuid: String,
        taskUuid: String,
        hardDelete: Boolean
    ): Result<DeletedTaskInfoDto> = runCatching {
        val task = local.getTasksOnce(boardUuid)
            .find { it.uuid == taskUuid || it.serverUuid == taskUuid }
            ?: return@runCatching DeletedTaskInfoDto("Unknown task", removedFromBoard = true)

        var remoteResult: DeletedTaskInfoDto? = null
        val boardServerUuid = boardLocal.getByLocalUuid(boardUuid)?.serverUuid

        if (task.serverUuid != null && boardServerUuid != null) {
            try {
                remoteResult = remote.deleteTask(boardServerUuid, task.serverUuid).getOrNull()
            } catch (e: Exception) {
                local.updateTask(task.copy(isDeleted = true, isSynced = false))
            }
        }

        if (hardDelete) {
            local.deleteTask(task)
        } else {
            val isSynced = remoteResult != null
            local.updateTask(task.copy(isDeleted = true, isSynced = isSynced))
        }

        remoteResult ?: DeletedTaskInfoDto(title = task.title, removedFromBoard = true)
    }

    override suspend fun getAllTasks(): List<Task> =
        local.getAllTasks().map(TaskMapper::fromEntity)

    override suspend fun getUnsyncedTasks(): List<Task> =
        local.getUnsyncedTasks().map(TaskMapper::fromEntity)

    override suspend fun getDeletedTasks(): List<Task> =
        local.getDeletedTasks().map(TaskMapper::fromEntity)
}
