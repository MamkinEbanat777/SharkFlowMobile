package com.example.sharkflow.worker.deadline

import android.content.*
import androidx.work.*
import com.example.sharkflow.utils.AppLog
import java.util.concurrent.TimeUnit

class SnoozeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val taskUuid = intent?.getStringExtra("snooze_task_uuid") ?: run {
            AppLog.d("SnoozeReceiver", "onReceive: no task uuid")
            return
        }
        val minutes = intent.getIntExtra("snooze_minutes", 60)

        val work = OneTimeWorkRequestBuilder<DeadlineReminderWorker>()
            .setInitialDelay(minutes.toLong(), TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueue(work)
        AppLog.d("SnoozeReceiver", "Snooze for $taskUuid for $minutes minutes")
    }
}
