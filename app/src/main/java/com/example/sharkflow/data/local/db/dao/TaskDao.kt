package com.example.sharkflow.data.local.db.dao

import androidx.room.*
import com.example.sharkflow.data.local.db.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE boardId = :boardId AND isDeleted = 0")
    fun getTasksForBoard(boardId: Int): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE boardId = :boardId")
    suspend fun clearTasksForBoard(boardId: Int)
}
