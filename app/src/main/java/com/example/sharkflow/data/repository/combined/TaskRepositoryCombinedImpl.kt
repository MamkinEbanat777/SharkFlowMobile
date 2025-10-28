package com.example.sharkflow.data.repository.combined

import com.example.sharkflow.core.system.AppLog
import com.example.sharkflow.data.api.dto.task.*
import com.example.sharkflow.data.local.db.entities.TaskEntity
import com.example.sharkflow.data.mapper.TaskMapper
import com.example.sharkflow.data.repository.local.TaskLocalRepositoryImpl
import com.example.sharkflow.data.repository.remote.TaskRepositoryImpl
import com.example.sharkflow.domain.model.Task
import com.example.sharkflow.domain.repository.TaskRepositoryCombined
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import java.time.Instant

class TaskRepositoryCombinedImpl @Inject constructor(
    private val local: TaskLocalRepositoryImpl,
    private val remote: TaskRepositoryImpl
) : TaskRepositoryCombined {

    override fun getTasksFlow(boardUuid: String): Flow<List<Task>> =
        local.getTasksFlow(boardUuid).map { it.map(TaskMapper::fromEntity) }

    override suspend fun refreshTasks(boardUuid: String) {
        val remoteRes = remote.getTasks(boardUuid).getOrNull() ?: return

        val localList = local.getTasksFlow(boardUuid).first()
        val localByServer =
            localList.filter { it.serverUuid != null }.associateBy { it.serverUuid!! }

        val toInsertOrUpdate = mutableListOf<TaskEntity>()
        remoteRes.forEach { remoteTask ->
            val serverUuid = remoteTask.serverUuid
            val existing = serverUuid?.let { localByServer[it] }
            if (existing != null) {
                toInsertOrUpdate.add(
                    existing.copy(
                        title = remoteTask.title,
                        description = remoteTask.description,
                        status = remoteTask.status.name,
                        priority = remoteTask.priority.name,
                        dueDate = remoteTask.dueDate,
                        createdAt = remoteTask.createdAt,
                        updatedAt = remoteTask.updatedAt,
                        isSynced = true
                    )
                )
            } else {
                val unsyncedLocal = localList.find {
                    it.serverUuid == null &&
                            !it.isDeleted &&
                            it.title == remoteTask.title &&
                            (it.description == remoteTask.description || (it.description.isNullOrBlank() && remoteTask.description.isNullOrBlank())) &&
                            (it.dueDate == remoteTask.dueDate)
                }

                val uuidToUse = unsyncedLocal?.uuid ?: java.util.UUID.randomUUID().toString()
                toInsertOrUpdate.add(
                    TaskEntity(
                        uuid = uuidToUse,
                        serverUuid = serverUuid,
                        title = remoteTask.title,
                        description = remoteTask.description,
                        boardUuid = boardUuid,
                        status = remoteTask.status.name,
                        priority = remoteTask.priority.name,
                        dueDate = remoteTask.dueDate,
                        createdAt = remoteTask.createdAt,
                        updatedAt = remoteTask.updatedAt,
                        isSynced = true
                    )
                )
            }
        }

        local.insertOrUpdateTasks(toInsertOrUpdate)
    }

    override suspend fun createTask(
        boardUuid: String,
        createDto: CreateTaskRequestDto,
        localUuid: String?
    ): Result<Task> = runCatching {
        val existingLocal = localUuid?.let { local.getByLocalUuid(it) }

        if (existingLocal != null) {
            val tempUpdated = existingLocal.copy(
                title = createDto.title,
                description = createDto.description,
                status = createDto.status.name,
                priority = createDto.priority.name,
                dueDate = createDto.dueDate,
                isSynced = false,
                isDeleted = false,
            )
            local.updateTask(tempUpdated)

            val remoteRes = try {
                remote.createTask(boardUuid, createDto).getOrNull()
            } catch (e: Exception) {
                null
            }

            if (remoteRes != null) {
                val final = tempUpdated.copy(serverUuid = remoteRes.serverUuid, isSynced = true)
                local.updateTask(final)
                return@runCatching TaskMapper.fromEntity(final)

            } else {
                return@runCatching TaskMapper.fromEntity(tempUpdated)
            }
        }

        val localGeneratedUuid = java.util.UUID.randomUUID().toString()
        val tempEntity = TaskEntity(
            uuid = localGeneratedUuid,
            serverUuid = null,
            title = createDto.title,
            description = createDto.description,
            boardUuid = boardUuid,
            status = createDto.status.name,
            priority = createDto.priority.name,
            isSynced = false,
            dueDate = createDto.dueDate,
            createdAt = createDto.createdAt ?: Instant.now().toString(),
            updatedAt = createDto.updatedAt ?: Instant.now().toString(),
        )
        local.insertOrUpdateTasks(listOf(tempEntity))

        val remoteRes = try {
            remote.createTask(boardUuid, createDto).getOrNull()
        } catch (e: Exception) {
            null
        }

        if (remoteRes != null) {
            val updated = tempEntity.copy(serverUuid = remoteRes.serverUuid, isSynced = true)
            local.updateTask(updated)
            return@runCatching TaskMapper.fromEntity(updated)
        } else {
            return@runCatching TaskMapper.fromEntity(tempEntity)
        }

    }

    override suspend fun updateTask(
        boardUuid: String,
        taskUuid: String,
        update: UpdateTaskRequestDto
    ): Result<Task> = runCatching {
        val localEntity =
            local.getByLocalUuid(taskUuid) ?: throw Exception("Task not found locally")
        val serverUuid = localEntity.serverUuid

        if (serverUuid == null) {
            val updated = localEntity.copy(
                title = update.title ?: localEntity.title,
                description = update.description ?: localEntity.description,
                status = update.status?.name ?: localEntity.status,
                priority = update.priority?.name ?: localEntity.priority,
                dueDate = update.dueDate ?: localEntity.dueDate,
                updatedAt = Instant.now().toString(),
                isSynced = false
            )
            local.updateTask(updated)
            return@runCatching TaskMapper.fromEntity(updated)
        }

        val remoteUpdatedDto = remote.updateTask(boardUuid, serverUuid, update).getOrNull()
        val mergedEntity = localEntity.copy(
            title = remoteUpdatedDto?.title ?: update.title ?: localEntity.title,
            description = remoteUpdatedDto?.description ?: update.description
            ?: localEntity.description,
            status = remoteUpdatedDto?.status?.name ?: update.status?.name ?: localEntity.status,
            priority = remoteUpdatedDto?.priority?.name ?: update.priority?.name
            ?: localEntity.priority,
            updatedAt = Instant.now().toString(),
            dueDate = remoteUpdatedDto?.dueDate ?: update.dueDate ?: localEntity.dueDate,
            isSynced = remoteUpdatedDto != null,
            isDeleted = false
        )
        local.updateTask(mergedEntity)
        return@runCatching TaskMapper.fromEntity(mergedEntity)
    }

    override suspend fun deleteTask(
        boardUuid: String,
        taskUuid: String,
        hardDelete: Boolean
    ): Result<DeletedTaskInfoDto> = runCatching {
        AppLog.d("TaskRepoCombined", "ТУТ ЭЭЭ ДА")

        val localEntity = local.getTasksOnce(boardUuid)
            .find { it.uuid == taskUuid || it.serverUuid == taskUuid }
            ?: return@runCatching DeletedTaskInfoDto(
                title = "Unknown task",
                removedFromBoard = true
            )

        var remoteResult: DeletedTaskInfoDto? = null

        if (localEntity.serverUuid != null) {
            AppLog.d(
                "TaskRepoCombined",
                "Attempting remote.deleteTask for local ${localEntity.uuid} -> server ${localEntity.serverUuid}"
            )
            try {
                remoteResult = remote.deleteTask(boardUuid, localEntity.serverUuid).getOrThrow()
                AppLog.d(
                    "TaskRepoCombined",
                    "Remote delete succeeded for serverUuid=${localEntity.serverUuid}: $remoteResult"
                )
            } catch (e: Exception) {
                AppLog.e(
                    "TaskRepoCombined",
                    "Remote delete failed for serverUuid=${localEntity.serverUuid}",
                    e
                )
                local.updateTask(localEntity.copy(isDeleted = true, isSynced = false))
            }
        }

        if (hardDelete) {
            local.deleteTask(localEntity)
        } else {
            local.updateTask(localEntity.copy(isDeleted = true, isSynced = true))
        }


        remoteResult ?: DeletedTaskInfoDto(
            title = localEntity.title,
            removedFromBoard = true
        )
    }

    override suspend fun getAllTasks(): List<Task> = local.getAllTasks().map(TaskMapper::fromEntity)
    override suspend fun getUnsyncedTasks(): List<Task> =
        local.getUnsyncedTasks().map(TaskMapper::fromEntity)

    override suspend fun getDeletedTasks(): List<Task> =
        local.getDeletedTasks().map(TaskMapper::fromEntity)
}
