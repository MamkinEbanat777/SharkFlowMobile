package com.example.sharkflow.worker.deadline

import android.content.*
import androidx.work.*
import java.util.concurrent.TimeUnit

class SnoozeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val taskUuid = intent?.getStringExtra("snooze_task_uuid") ?: run {
            return
        }
        val minutes = intent.getIntExtra("snooze_minutes", 60)

        val work = OneTimeWorkRequestBuilder<DeadlineReminderWorker>()
            .setInitialDelay(minutes.toLong(), TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueue(work)
    }
}
