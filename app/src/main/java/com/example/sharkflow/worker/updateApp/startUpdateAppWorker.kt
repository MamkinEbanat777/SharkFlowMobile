package com.example.sharkflow.worker.updateApp

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

fun startUpdateAppWorker(context: Context, manifestUrl: String) {
    val input = workDataOf("manifestUrl" to manifestUrl)
    val work = PeriodicWorkRequestBuilder<UpdateCheckWorker>(24, TimeUnit.HOURS)
        .setInputData(input)
        .setInitialDelay(1, TimeUnit.MINUTES)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "sharkflow_update_check",
        ExistingPeriodicWorkPolicy.KEEP,
        work
    )
}
