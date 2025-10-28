package com.example.sharkflow.data.repository.remote

import com.example.sharkflow.core.system.AppLog
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
        AppLog.d("RemoteRepo", "Calling API deleteTask(board=$boardUuid, id=$taskUuid)")
        val resp = api.deleteTask(boardUuid, taskUuid)
        AppLog.d(
            "RemoteRepo",
            "HTTP response code=${resp.code()}, isSuccessful=${resp.isSuccessful}"
        )
        val body = resp.body()
        AppLog.d("RemoteRepo", "HTTP body=${body}")
        if (!resp.isSuccessful) {
            val err = resp.errorBody()?.string()
            AppLog.e("RemoteRepo", "Delete failed with code=${resp.code()}, error=$err")
            throw Exception("Delete failed: ${resp.code()}")
        }
        resp.body()?.deletedTask ?: throw Exception("Delete failed: empty body")
    }.onFailure { e ->
        AppLog.e("RemoteRepo", "deleteTask error", e)
    }

}
