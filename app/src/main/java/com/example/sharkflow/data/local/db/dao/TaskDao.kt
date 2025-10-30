package com.example.sharkflow.data.local.db.dao

import androidx.room.*
import com.example.sharkflow.data.local.db.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE boardUuid = :boardUuid AND isDeleted = 0")
    fun getTasksForBoard(boardUuid: String): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE boardUuid = :boardUuid")
    suspend fun clearTasksForBoard(boardUuid: String)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE serverUuid = :serverUuid LIMIT 1")
    suspend fun getByServerUuid(serverUuid: String): TaskEntity?

    @Query("SELECT * FROM tasks WHERE uuid = :localUuid LIMIT 1")
    suspend fun getByLocalUuid(localUuid: String): TaskEntity?

    @Query("SELECT * FROM tasks WHERE (isSynced = 0) OR (serverUuid IS NULL)")
    suspend fun getUnsyncedTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE isDeleted = 1")
    suspend fun getDeletedTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE boardUuid = :boardUuid")
    suspend fun getTasksForBoardOnce(boardUuid: String): List<TaskEntity>
}
