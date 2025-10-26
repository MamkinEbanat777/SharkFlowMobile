package com.example.sharkflow.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.sharkflow.data.api.dto.task.*
import com.example.sharkflow.data.repository.combined.TaskRepositoryCombinedImpl
import com.example.sharkflow.utils.AppLog
import dagger.assisted.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicBoolean

@HiltWorker
class TaskSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: TaskRepositoryCombinedImpl
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        withContext(Dispatchers.IO) {

            val hasErrors = AtomicBoolean(false)
            val semaphore = Semaphore(5)

            val deletedTasks = repository.getDeletedTasks()
            deletedTasks.map { task ->
                async {
                    semaphore.withPermit {
                        try {
                            repository.deleteTask(task.boardUuid, task.uuid, hardDelete = false)
                            AppLog.d("TaskSyncWorker", "Deleted task ${task.uuid}")
                        } catch (e: Exception) {
                            hasErrors.set(true)
                            AppLog.e("TaskSyncWorker", "Failed to delete task ${task.uuid}", e)
                        }
                    }
                }
            }.awaitAll()

            val unsyncedTasks = repository.getUnsyncedTasks().filter { !it.isDeleted }
            unsyncedTasks.map { task ->
                async {
                    semaphore.withPermit {
                        try {
                            if (task.serverUuid == null) {
                                repository.createTask(
                                    task.boardUuid,
                                    CreateTaskRequestDto(
                                        title = task.title,
                                        description = task.description,
                                        dueDate = task.dueDate,
                                        status = task.status,
                                        priority = task.priority
                                    ),
                                    localUuid = task.uuid // <- ключевая правка
                                )
                                AppLog.d("TaskSyncWorker", "Created task ${task.uuid}")
                            } else {
                                repository.updateTask(
                                    task.boardUuid,
                                    task.uuid,
                                    UpdateTaskRequestDto(
                                        title = task.title,
                                        description = task.description,
                                        dueDate = task.dueDate,
                                        status = task.status,
                                        priority = task.priority
                                    )
                                )
                                AppLog.d("TaskSyncWorker", "Updated task ${task.uuid}")
                            }
                        } catch (e: Exception) {
                            hasErrors.set(true)
                            AppLog.e("TaskSyncWorker", "Failed to sync task ${task.uuid}", e)
                        }
                    }
                }
            }.awaitAll()


            if (hasErrors.get()) Result.retry() else Result.success()
        }
    }
}
