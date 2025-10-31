package com.example.sharkflow.worker

import android.content.Context
import androidx.work.*
import com.example.sharkflow.worker.board.BoardSyncWorker
import com.example.sharkflow.worker.task.TaskSyncWorker
import java.util.concurrent.TimeUnit

fun startFullSyncChain(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val boardSync = OneTimeWorkRequestBuilder<BoardSyncWorker>()
        .setConstraints(constraints)
        .build()

    val taskSync = OneTimeWorkRequestBuilder<TaskSyncWorker>()
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(context)
        .beginUniqueWork(
            "full_sync_chain_now",
            ExistingWorkPolicy.KEEP,
            boardSync
        )
        .then(taskSync)
        .enqueue()

    val periodicBoards = PeriodicWorkRequestBuilder<BoardSyncWorker>(15, TimeUnit.MINUTES)
        .setConstraints(constraints)
        .build()

    val periodicTasks = PeriodicWorkRequestBuilder<TaskSyncWorker>(15, TimeUnit.MINUTES)
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "board_sync_periodic",
        ExistingPeriodicWorkPolicy.KEEP,
        periodicBoards
    )

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "task_sync_periodic",
        ExistingPeriodicWorkPolicy.KEEP,
        periodicTasks
    )
}
