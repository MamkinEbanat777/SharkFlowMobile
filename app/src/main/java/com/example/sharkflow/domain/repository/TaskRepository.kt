package com.example.sharkflow.domain.repository

import com.example.sharkflow.data.api.dto.task.*
import com.example.sharkflow.domain.model.Task

interface TaskRepository {

    suspend fun getTasks(boardUuid: String): Result<List<Task>>

    suspend fun createTask(boardUuid: String, createDto: CreateTaskRequestDto): Result<Task>

    suspend fun updateTask(
        boardUuid: String,
        taskUuid: String,
        update: UpdateTaskRequestDto
    ): Result<UpdateTaskRequestDto>

    suspend fun deleteTask(boardUuid: String, taskUuid: String): Result<DeletedTaskInfoDto>
}
