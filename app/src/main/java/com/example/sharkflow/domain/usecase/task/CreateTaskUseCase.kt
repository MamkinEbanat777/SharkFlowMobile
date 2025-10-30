package com.example.sharkflow.domain.usecase.task

import com.example.sharkflow.data.api.dto.task.CreateTaskRequestDto
import com.example.sharkflow.data.mapper.TaskMapper
import com.example.sharkflow.data.repository.local.TaskLocalRepositoryImpl
import com.example.sharkflow.domain.model.Task
import com.example.sharkflow.domain.repository.*
import jakarta.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val repositoryCombined: TaskRepositoryCombined,
    private val boardRepositoryCombined: BoardRepositoryCombined,
    private val local: TaskLocalRepositoryImpl
) {
    suspend operator fun invoke(
        boardUuid: String,
        createDto: CreateTaskRequestDto,
        localUuid: String?
    ): Result<Task> = runCatching {
        val existingLocalEntity = localUuid?.let { local.getByLocalUuid(it) }
        val tempEntity = existingLocalEntity?.copy(
            title = createDto.title,
            description = createDto.description,
            status = createDto.status.name,
            priority = createDto.priority.name,
            dueDate = createDto.dueDate,
            isSynced = false,
            isDeleted = false,
            updatedAt = createDto.updatedAt ?: java.time.Instant.now().toString()
        ) ?: TaskMapper.toLocalEntityForCreate(boardUuid, createDto, existingUuid = localUuid)

        local.insertOrUpdateTasks(listOf(tempEntity))

        val remoteRes = try {
            repositoryCombined.createTask(boardUuid, createDto, tempEntity.uuid).getOrNull()
        } catch (e: Exception) {
            null
        }
        try {
            boardRepositoryCombined.refreshBoards()
        } catch (e: Exception) {
            null
        }

        val mergedEntity = TaskMapper.mergeLocalWithRemoteAfterCreate(
            tempEntity,
            remoteRes?.serverUuid ?: remoteRes?.uuid
        )

        local.updateTask(mergedEntity)

        TaskMapper.fromEntity(mergedEntity)
    }
}
