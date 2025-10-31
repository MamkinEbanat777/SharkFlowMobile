package com.example.sharkflow.worker.task

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.sharkflow.core.system.AppLog
import com.example.sharkflow.data.api.dto.task.*
import com.example.sharkflow.domain.usecase.task.*
import dagger.assisted.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicBoolean

@HiltWorker
class TaskSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val createTaskUseCase: CreateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getUnsyncedTasksUseCase: GetUnsyncedTasksUseCase,
    private val getDeletedTasksUseCase: GetDeletedTasksUseCase,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        withContext(Dispatchers.IO) {

            val hasErrors = AtomicBoolean(false)
            val semaphore = Semaphore(5)

            val deletedTasks = getDeletedTasksUseCase()

            deletedTasks.map { task ->
                async {
                    semaphore.withPermit {
                        try {
                            val hardDelete = task.serverUuid == null
                            val result =
                                deleteTaskUseCase(task.boardUuid, task.uuid, hardDelete)
                        } catch (e: Exception) {
                            hasErrors.set(true)
                            AppLog.e(
                                "TaskSyncWorker",
                                "Failed to delete task ${task.uuid} on server",
                                e
                            )
                        }
                    }
                }
            }.awaitAll()

            val unsyncedTasks = getUnsyncedTasksUseCase().filter { !it.isDeleted }
            unsyncedTasks.map { task ->
                async {
                    semaphore.withPermit {
                        try {
                            if (task.serverUuid == null) {
                                createTaskUseCase(
                                    task.boardUuid, CreateTaskRequestDto(
                                        title = task.title,
                                        description = task.description,
                                        dueDate = task.dueDate,
                                        status = task.status,
                                        priority = task.priority
                                    ),
                                    localUuid = task.uuid
                                )
                            } else {
                                updateTaskUseCase(
                                    task.boardUuid, task.uuid, UpdateTaskRequestDto(
                                        title = task.title,
                                        description = task.description,
                                        dueDate = task.dueDate,
                                        status = task.status,
                                        priority = task.priority
                                    )
                                )
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
