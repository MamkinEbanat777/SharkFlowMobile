package com.example.sharkflow.worker.board

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

fun startBoardSyncWorker(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val oneTimeWork = OneTimeWorkRequestBuilder<BoardSyncWorker>()
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(context).enqueueUniqueWork(
        "board_sync_now",
        ExistingWorkPolicy.KEEP,
        oneTimeWork
    )

    val periodicWork = PeriodicWorkRequestBuilder<BoardSyncWorker>(15, TimeUnit.MINUTES)
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "board_sync_periodic",
        ExistingPeriodicWorkPolicy.KEEP,
        periodicWork
    )
}
