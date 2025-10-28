package com.example.sharkflow.data.mapper

import com.example.sharkflow.data.api.dto.task.*
import com.example.sharkflow.data.local.db.entities.TaskEntity
import com.example.sharkflow.domain.model.*

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


}
