package com.example.sharkflow.worker.deadline

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

fun startDeadlineReminderWorker(context: Context) {
    val work = PeriodicWorkRequestBuilder<DeadlineReminderWorker>(
        15, TimeUnit.MINUTES
    ).build()

    val oneTimeWork = OneTimeWorkRequestBuilder<DeadlineReminderWorker>()
        .setInitialDelay(5, TimeUnit.SECONDS)
        .build()

    WorkManager.getInstance(context).enqueue(oneTimeWork)

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "deadline_reminder_worker",
        ExistingPeriodicWorkPolicy.KEEP,
        work
    )
}
