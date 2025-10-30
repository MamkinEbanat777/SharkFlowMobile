package com.example.sharkflow.domain.usecase.task

import com.example.sharkflow.data.api.dto.task.DeletedTaskInfoDto
import com.example.sharkflow.data.repository.local.TaskLocalRepositoryImpl
import com.example.sharkflow.domain.repository.*
import jakarta.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repositoryCombined: TaskRepositoryCombined,
    private val boardRepositoryCombined: BoardRepositoryCombined,
    private val local: TaskLocalRepositoryImpl
) {
    suspend operator fun invoke(
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
                remoteResult =
                    repositoryCombined.deleteTask(boardUuid, localEntity.serverUuid)
                        .getOrThrow()
            } catch (e: Exception) {
                local.updateTask(localEntity.copy(isDeleted = true, isSynced = false))
            }
        }

        try {
            boardRepositoryCombined.refreshBoards()
        } catch (e: Exception) {
            null
        }

        if (hardDelete) {
            local.deleteTask(localEntity)
        } else {
            if (remoteResult != null) {
                local.updateTask(localEntity.copy(isDeleted = true, isSynced = true))
            } else {
                local.updateTask(localEntity.copy(isDeleted = true, isSynced = false))
            }
        }

        remoteResult ?: DeletedTaskInfoDto(
            title = localEntity.title,
            removedFromBoard = true
        )
    }
}
