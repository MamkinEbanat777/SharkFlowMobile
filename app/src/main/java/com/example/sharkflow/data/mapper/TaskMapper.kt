package com.example.sharkflow.data.mapper

import com.example.sharkflow.data.api.dto.task.*
import com.example.sharkflow.data.local.db.entities.TaskEntity
import com.example.sharkflow.domain.model.*
import java.time.Instant
import java.util.UUID

object TaskMapper {
    private fun toStatus(status: String?): TaskStatus = when (status) {
        "PENDING" -> TaskStatus.PENDING
        "IN_PROGRESS" -> TaskStatus.IN_PROGRESS
        "COMPLETED" -> TaskStatus.COMPLETED
        "CANCELLED" -> TaskStatus.CANCELLED
        else -> TaskStatus.PENDING
    }

    private fun toPriority(priority: String?): TaskPriority = when (priority) {
        "LOW" -> TaskPriority.LOW
        "MEDIUM" -> TaskPriority.MEDIUM
        "HIGH" -> TaskPriority.HIGH
        else -> TaskPriority.MEDIUM
    }

    fun fromResponseDto(dto: TaskResponseDto, boardUuid: String): Task {
        return Task(
            uuid = java.util.UUID.randomUUID().toString(),
            serverUuid = dto.uuid,
            title = dto.title,
            description = dto.description,
            boardUuid = boardUuid,
            status = toStatus(dto.status),
            priority = toPriority(dto.priority),
            isDeleted = false,
            isSynced = true,
            dueDate = dto.dueDate,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt
        )
    }

    fun toEntity(domain: Task): TaskEntity =
        TaskEntity(
            uuid = domain.uuid,
            serverUuid = domain.serverUuid,
            title = domain.title,
            description = domain.description,
            boardUuid = domain.boardUuid,
            status = domain.status.name,
            priority = domain.priority.name,
            isDeleted = domain.isDeleted,
            isSynced = domain.isSynced,
            dueDate = domain.dueDate,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )

    fun fromEntity(entity: TaskEntity): Task =
        Task(
            uuid = entity.uuid,
            serverUuid = entity.serverUuid,
            title = entity.title,
            description = entity.description,
            boardUuid = entity.boardUuid,
            status = toStatus(entity.status),
            priority = toPriority(entity.priority),
            isDeleted = entity.isDeleted,
            isSynced = entity.isSynced,
            dueDate = entity.dueDate,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )

    fun mergeUpdate(current: Task, partial: UpdateTaskRequestDto): Task =
        current.copy(
            title = partial.title ?: current.title,
            description = partial.description ?: current.description,
            status = partial.status ?: current.status,
            priority = partial.priority ?: current.priority,
            dueDate = partial.dueDate ?: current.dueDate
        )

    fun toEntityFromDto(dto: TaskResponseDto, boardUuid: String): TaskEntity {
        return TaskEntity(
            uuid = java.util.UUID.randomUUID().toString(),
            serverUuid = dto.uuid,
            title = dto.title,
            description = dto.description,
            boardUuid = boardUuid,
            status = dto.status ?: "PENDING",
            priority = dto.priority ?: "MEDIUM",
            isDeleted = false,
            isSynced = true,
            dueDate = dto.dueDate,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt
        )
    }

    fun mergeEntityWithUpdate(
        entity: TaskEntity,
        update: UpdateTaskRequestDto,
        remote: UpdateTaskRequestDto? = null
    ): TaskEntity {
        return entity.copy(
            title = remote?.title ?: update.title ?: entity.title,
            description = remote?.description ?: update.description ?: entity.description,
            status = remote?.status?.name ?: update.status?.name ?: entity.status,
            priority = remote?.priority?.name ?: update.priority?.name ?: entity.priority,
            dueDate = remote?.dueDate ?: update.dueDate ?: entity.dueDate,
            updatedAt = Instant.now().toString(),
            isSynced = remote != null,
            isDeleted = false
        )
    }

    fun toLocalEntityForCreate(
        boardUuid: String,
        createDto: CreateTaskRequestDto,
        existingUuid: String? = null
    ): TaskEntity {
        val now = Instant.now().toString()
        return TaskEntity(
            uuid = existingUuid ?: UUID.randomUUID().toString(),
            serverUuid = null,
            title = createDto.title,
            description = createDto.description,
            boardUuid = boardUuid,
            status = createDto.status.name,
            priority = createDto.priority.name,
            isDeleted = false,
            isSynced = false,
            dueDate = createDto.dueDate,
            createdAt = createDto.createdAt ?: now,
            updatedAt = createDto.updatedAt ?: now
        )
    }

    fun mergeLocalWithRemoteAfterCreate(
        local: TaskEntity,
        remoteServerUuid: String?
    ): TaskEntity {
        return if (remoteServerUuid != null) {
            local.copy(
                serverUuid = remoteServerUuid,
                isSynced = true
            )
        } else {
            local.copy(isSynced = false)
        }
    }


    fun mergeRemoteWithLocalList(
        boardUuid: String,
        remoteTasks: List<Task>,
        localTasks: List<TaskEntity>
    ): List<TaskEntity> {
        val localByServer = localTasks
            .filter { it.serverUuid != null }
            .associateBy { it.serverUuid }

        val toInsertOrUpdate = mutableListOf<TaskEntity>()

        remoteTasks.forEach { remote ->
            val serverUuid =
                remote.serverUuid ?: throw IllegalStateException("remote task must have serverUuid")
            val existing = localByServer[serverUuid]

            if (existing != null) {
                toInsertOrUpdate.add(
                    existing.copy(
                        title = remote.title,
                        description = remote.description,
                        status = remote.status.name,
                        priority = remote.priority.name,
                        dueDate = remote.dueDate,
                        createdAt = remote.createdAt,
                        updatedAt = remote.updatedAt,
                        isSynced = true
                    )
                )
            } else {
                val unsyncedLocal = localTasks.find {
                    it.serverUuid == null &&
                            !it.isDeleted &&
                            it.title == remote.title &&
                            (it.description == remote.description ||
                                    (it.description.isNullOrBlank() && remote.description.isNullOrBlank())) &&
                            it.dueDate == remote.dueDate
                }

                val uuidToUse = unsyncedLocal?.uuid ?: java.util.UUID.randomUUID().toString()

                toInsertOrUpdate.add(
                    TaskEntity(
                        uuid = uuidToUse,
                        serverUuid = serverUuid,
                        title = remote.title,
                        description = remote.description,
                        boardUuid = boardUuid,
                        status = remote.status.name,
                        priority = remote.priority.name,
                        dueDate = remote.dueDate,
                        createdAt = remote.createdAt,
                        updatedAt = remote.updatedAt,
                        isSynced = true,
                        isDeleted = false
                    )
                )
            }
        }

        return toInsertOrUpdate
    }

}
