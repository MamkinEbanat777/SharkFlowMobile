package com.example.sharkflow.worker.task

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

fun startTaskSyncWorker(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val oneTimeWork = OneTimeWorkRequestBuilder<TaskSyncWorker>()
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(context).enqueueUniqueWork(
        "task_sync_now",
        ExistingWorkPolicy.KEEP,
        oneTimeWork
    )

    val periodicWork = PeriodicWorkRequestBuilder<TaskSyncWorker>(15, TimeUnit.MINUTES)
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "task_sync_periodic",
        ExistingPeriodicWorkPolicy.KEEP,
        periodicWork
    )
}
