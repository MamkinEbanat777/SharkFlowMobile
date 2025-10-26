package com.example.sharkflow.data.repository.remote

import com.example.sharkflow.data.api.TaskApi
import com.example.sharkflow.data.api.dto.task.*
import com.example.sharkflow.data.mapper.TaskMapper
import com.example.sharkflow.domain.model.Task
import com.example.sharkflow.domain.repository.TaskRepository
import jakarta.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val api: TaskApi
) : TaskRepository {

    override suspend fun getTasks(boardUuid: String): Result<List<Task>> = runCatching {
        val resp = api.getTasks(boardUuid)
        if (resp.isSuccessful) resp.body()?.tasks?.map { TaskMapper.fromResponseDto(it, boardUuid) }
            ?: emptyList()
        else throw Exception("Failed to fetch tasks")
    }

    override suspend fun createTask(
        boardUuid: String,
        createDto: CreateTaskRequestDto
    ): Result<Task> = runCatching {
        println("📌 [Remote createTask] Отправка запроса: boardUuid=$boardUuid, title='${createDto.title}', description='${createDto.description}'")

        val resp = api.createTask(boardUuid, createDto)
        if (!resp.isSuccessful) throw Exception("Create failed")

        val body = resp.body() ?: throw Exception("Response body is null")
        val taskDto = body.task
        val task = TaskMapper.fromResponseDto(taskDto, boardUuid)

        println("✅ [Remote createTask] Таска получена с сервера: uuid=${task.uuid}, title=${task.title}")
        task
    }.onFailure { e ->
        println("❌ [Remote createTask] Ошибка при создании таски: ${e.message}")
    }


    override suspend fun updateTask(
        boardUuid: String,
        taskUuid: String,
        update: UpdateTaskRequestDto
    ): Result<UpdateTaskRequestDto> = runCatching {
        val resp = api.updateTask(boardUuid, taskUuid, update)
        if (!resp.isSuccessful) throw Exception("Update failed")
        resp.body()?.updated ?: throw Exception("Update failed")
    }

    override suspend fun deleteTask(
        boardUuid: String,
        taskUuid: String
    ): Result<DeletedTaskInfoDto> = runCatching {
        val resp = api.deleteTask(boardUuid, taskUuid)
        if (!resp.isSuccessful) throw Exception("Delete failed")
        resp.body()?.deletedTask ?: throw Exception("Delete failed")
    }
}
