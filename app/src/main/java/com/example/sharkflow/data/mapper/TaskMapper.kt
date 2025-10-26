package com.example.sharkflow.data.mapper

import com.example.sharkflow.data.api.dto.task.*
import com.example.sharkflow.data.local.db.entities.TaskEntity
import com.example.sharkflow.domain.model.Task

object TaskMapper {
    private fun toStatus(status: String?): Status = when (status) {
        "PENDING" -> Status.PENDING
        "IN_PROGRESS" -> Status.IN_PROGRESS
        "COMPLETED" -> Status.COMPLETED
        "CANCELLED" -> Status.CANCELLED
        else -> Status.PENDING
    }

    private fun toPriority(priority: String?): Priority = when (priority) {
        "LOW" -> Priority.LOW
        "MEDIUM" -> Priority.MEDIUM
        "HIGH" -> Priority.HIGH
        else -> Priority.MEDIUM
    }

    fun fromResponseDto(dto: TaskResponseDto, boardUuid: String): Task = Task(
        uuid = dto.uuid,
        title = dto.title,
        description = dto.description,
        boardUuid = boardUuid,
        status = toStatus(dto.status),
        priority = toPriority(dto.priority),
        isDeleted = false,
        isSynced = true,
        dueDate = dto.dueDate
    )

    fun toEntity(domain: Task): TaskEntity = TaskEntity(
        uuid = domain.uuid,
        title = domain.title,
        description = domain.description,
        boardUuid = domain.boardUuid,
        status = domain.status.name,
        priority = domain.priority.name,
        isDeleted = domain.isDeleted,
        isSynced = domain.isSynced,
        dueDate = domain.dueDate
    )

    fun fromEntity(entity: TaskEntity): Task = Task(
        uuid = entity.uuid,
        title = entity.title,
        description = entity.description,
        boardUuid = entity.boardUuid,
        status = toStatus(entity.status),
        priority = toPriority(entity.priority),
        isDeleted = entity.isDeleted,
        isSynced = entity.isSynced,
        dueDate = entity.dueDate
    )

    fun mergeUpdate(current: Task, partial: UpdateTaskRequestDto): Task =
        current.copy(
            title = partial.title ?: current.title,
            description = partial.description ?: current.description,
            status = partial.status ?: current.status,
            priority = partial.priority ?: current.priority,
            dueDate = partial.dueDate ?: current.dueDate
        )
}
