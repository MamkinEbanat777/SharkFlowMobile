package com.example.sharkflow.domain.repository

import com.example.sharkflow.data.api.dto.task.*
import com.example.sharkflow.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepositoryCombined {

    fun getTasksFlow(boardUuid: String): Flow<List<Task>>

    suspend fun refreshTasks(boardUuid: String)

    suspend fun createTask(
        boardUuid: String,
        createDto: CreateTaskRequestDto,
        localUuid: String? = null
    ): Result<Task>

    suspend fun updateTask(
        boardUuid: String,
        taskUuid: String,
        update: UpdateTaskRequestDto
    ): Result<Task>

    suspend fun deleteTask(
        boardUuid: String,
        taskUuid: String,
        hardDelete: Boolean = false
    ): Result<DeletedTaskInfoDto>

    suspend fun getUnsyncedTasks(): List<Task>
    suspend fun getDeletedTasks(): List<Task>
    suspend fun getAllTasks(): List<Task>
}
