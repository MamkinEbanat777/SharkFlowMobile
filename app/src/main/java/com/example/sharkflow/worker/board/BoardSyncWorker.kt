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
            AppLog.d("BoardSyncWorker", "doWork START")
            val hasErrors = AtomicBoolean(false)
            val semaphore = Semaphore(3)

            val deleted = getDeletedBoardsUseCase()
            AppLog.d(
                "BoardSyncWorker",
                "Deleted boards found: ${deleted.size} -> ${deleted.map { it.uuid + "/" + it.serverUuid }}"
            )

            // удаление
            deleted.map { board ->
                async {
                    semaphore.withPermit {
                        try {
                            val hardDelete = board.serverUuid == null
                            AppLog.d(
                                "BoardSyncWorker",
                                "Deleting board ${board.uuid} serverUuid=${board.serverUuid} hard=$hardDelete"
                            )
                            val res = deleteBoardUseCase(board.uuid, true)
                            AppLog.d("BoardSyncWorker", "delete result for ${board.uuid}: $res")
                        } catch (e: Exception) {
                            hasErrors.set(true)
                            AppLog.e("BoardSyncWorker", "Failed to delete board ${board.uuid}", e)
                        }
                    }
                }
            }.awaitAll()

            val unsynced = getUnsyncedBoardsUseCase().filter { !it.isDeleted }
            AppLog.d(
                "BoardSyncWorker",
                "Unsynced boards found: ${unsynced.size} -> ${unsynced.map { it.uuid + "/" + it.serverUuid + ":" + it.title }}"
            )

            unsynced.map { board ->
                async {
                    semaphore.withPermit {
                        try {
                            if (board.serverUuid == null) {
                                AppLog.d(
                                    "BoardSyncWorker",
                                    "Creating remote board for local ${board.uuid} (title='${board.title}')"
                                )
                                createBoardUseCase(board.title, board.color ?: "FFFFFF", board.uuid)
                                AppLog.d("BoardSyncWorker", "Created remote for ${board.uuid}")
                            } else {
                                val updateDto = UpdateBoardRequestDto(
                                    title = board.title,
                                    color = board.color,
                                    isPinned = board.isPinned,
                                    isFavorite = board.isFavorite
                                )
                                AppLog.d(
                                    "BoardSyncWorker",
                                    "Updating remote board serverUuid=${board.serverUuid} for local ${board.uuid}"
                                )
                                updateBoardUseCase(board.serverUuid, updateDto)
                                AppLog.d("BoardSyncWorker", "Updated remote for ${board.uuid}")
                            }
                        } catch (e: Exception) {
                            hasErrors.set(true)
                            AppLog.e("BoardSyncWorker", "Failed to sync board ${board.uuid}", e)
                        }
                    }
                }
            }.awaitAll()

            AppLog.d("BoardSyncWorker", "doWork END hasErrors=${hasErrors.get()}")
            if (hasErrors.get()) Result.retry() else Result.success()
        }
    }
}

