package com.example.sharkflow.data.repository.combined

import com.example.sharkflow.data.api.dto.task.*
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
        local.getTasksFlow(boardUuid)
            .map { it.map(TaskMapper::fromEntity) }

    override suspend fun refreshTasks(boardUuid: String) {
        val remoteTasks = remote.getTasks(boardUuid).getOrNull() ?: return
        local.insertOrUpdateTasks(remoteTasks.map(TaskMapper::toEntity))
    }

    override suspend fun createTask(
        boardUuid: String,
        createDto: CreateTaskRequestDto
    ): Result<Task> = runCatching {
        println("📌 [createTask] Начинаем создание таски: boardUuid=$boardUuid, title='${createDto.title}', description='${createDto.description}'")

        val task = remote.createTask(boardUuid, createDto).getOrNull()
        if (task == null) {
            println("⚠️ [createTask] Сервер не вернул таску, выбрасываем исключение")
            throw Exception("Failed to create task on server")
        }

        println("✅ [createTask] Таска получена с сервера: uuid=${task.uuid}, title=${task.title}")

        val entity = TaskMapper.toEntity(task)
        println("🔹 [createTask] Замаппили Task в TaskEntity: $entity")

        local.insertOrUpdateTasks(listOf(entity))
        println("💾 [createTask] Таска вставлена в локальную базу Room: uuid=${entity.uuid}")

        task
    }.onFailure { e ->
        println("❌ [createTask] Ошибка при создании таски: ${e.message}")
    }

    override suspend fun updateTask(
        boardUuid: String,
        taskUuid: String,
        update: UpdateTaskRequestDto
    ): Result<Task> = runCatching {
        val updatedFields = remote.updateTask(boardUuid, taskUuid, update).getOrNull()
        val currentEntity = local.getTasksFlow(boardUuid).first().find { it.uuid == taskUuid }
            ?: throw Exception("Task not found locally")
        val current = TaskMapper.fromEntity(currentEntity)

        val merged = if (updatedFields != null) {
            TaskMapper.mergeUpdate(current, updatedFields)
        } else {
            current.copy(
                title = update.title ?: current.title,
                description = update.description ?: current.description,
                status = update.status ?: current.status,
                priority = update.priority ?: current.priority,
                isSynced = false
            )
        }


        local.updateTask(TaskMapper.toEntity(merged))
        merged
    }

    override suspend fun deleteTask(
        boardUuid: String,
        taskUuid: String,
        hardDelete: Boolean
    ): Result<DeletedTaskInfoDto> = runCatching {
        val remoteResult = remote.deleteTask(boardUuid, taskUuid).getOrNull()
        val localEntity = local.getTasksFlow(boardUuid).first().find { it.uuid == taskUuid }

        if (localEntity != null) {
            if (hardDelete) {
                local.deleteTask(localEntity)
            } else {
                local.updateTask(localEntity.copy(isDeleted = true))
            }
        }

        remoteResult ?: DeletedTaskInfoDto(
            title = localEntity?.title ?: "Unknown task",
            removedFromBoard = true
        )
    }

}

