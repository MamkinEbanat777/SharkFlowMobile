package com.example.sharkflow.data.repository.local

import com.example.sharkflow.data.local.db.dao.TaskDao
import com.example.sharkflow.data.local.db.entities.TaskEntity
import jakarta.inject.Inject

class TaskLocalRepositoryImpl @Inject constructor(private val dao: TaskDao) {
    fun getTasksFlow(boardUuid: String) = dao.getTasksForBoard(boardUuid)
    suspend fun insertOrUpdateTasks(tasks: List<TaskEntity>) = dao.insertTasks(tasks)
    suspend fun updateTask(task: TaskEntity) = dao.updateTask(task)
    suspend fun deleteTask(task: TaskEntity) = dao.deleteTask(task)
    suspend fun clearTasksForBoard(boardUuid: String) = dao.clearTasksForBoard(boardUuid)
    suspend fun getByServerUuid(serverUuid: String) = dao.getByServerUuid(serverUuid)
    suspend fun getByLocalUuid(localUuid: String) = dao.getByLocalUuid(localUuid)
    suspend fun getUnsyncedTasks() = dao.getUnsyncedTasks()
    suspend fun getDeletedTasks() = dao.getDeletedTasks()

}
