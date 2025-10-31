package com.example.sharkflow.worker.board

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.sharkflow.core.system.AppLog
import com.example.sharkflow.data.api.dto.board.UpdateBoardRequestDto
import com.example.sharkflow.domain.usecase.board.*
import dagger.assisted.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicBoolean

@HiltWorker
class BoardSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val createBoardUseCase: CreateBoardUseCase,
    private val deleteBoardUseCase: DeleteBoardUseCase,
    private val updateBoardUseCase: UpdateBoardUseCase,
    private val getUnsyncedBoardsUseCase: GetUnsyncedBoardsUseCase,
    private val getDeletedBoardsUseCase: GetDeletedBoardsUseCase
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        withContext(Dispatchers.IO) {
            val hasErrors = AtomicBoolean(false)
            val semaphore = Semaphore(3)

            val deleted = getDeletedBoardsUseCase()

            // удаление
            deleted.map { board ->
                async {
                    semaphore.withPermit {
                        try {
                            deleteBoardUseCase(board.uuid)
                        } catch (e: Exception) {
                            hasErrors.set(true)
                            AppLog.e("BoardSyncWorker", "Failed to delete board ${board.uuid}", e)
                        }
                    }
                }
            }.awaitAll()

            val unsynced = getUnsyncedBoardsUseCase().filter { !it.isDeleted }

            unsynced.map { board ->
                async {
                    semaphore.withPermit {
                        try {
                            if (board.serverUuid == null) {
                                createBoardUseCase(board.title, board.color ?: "FFFFFF", board.uuid)
                            } else {
                                val updateDto = UpdateBoardRequestDto(
                                    title = board.title,
                                    color = board.color,
                                    isPinned = board.isPinned,
                                    isFavorite = board.isFavorite
                                )
                                updateBoardUseCase(board.serverUuid, updateDto)
                            }
                        } catch (e: Exception) {
                            hasErrors.set(true)
                            AppLog.e("BoardSyncWorker", "Failed to sync board ${board.uuid}", e)
                        }
                    }
                }
            }.awaitAll()

            if (hasErrors.get()) Result.retry() else Result.success()
        }
    }
}