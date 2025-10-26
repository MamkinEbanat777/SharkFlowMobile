package com.example.sharkflow.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

fun startTaskSyncWorker(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val testWork = OneTimeWorkRequestBuilder<TaskSyncWorker>().build()
    WorkManager.getInstance(context).enqueue(testWork)

    val syncWork = PeriodicWorkRequestBuilder<TaskSyncWorker>(15, TimeUnit.MINUTES)
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "task_sync",
        ExistingPeriodicWorkPolicy.KEEP,
        syncWork
    )
}
