package com.example.sharkflow.worker.deadline

import android.app.*
import android.content.*
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.sharkflow.MainActivity
import com.example.sharkflow.data.api.dto.task.*
import com.example.sharkflow.data.repository.combined.TaskRepositoryCombinedImpl
import com.example.sharkflow.domain.model.Task
import com.example.sharkflow.utils.AppLog
import dagger.assisted.*
import java.time.Instant
import java.util.concurrent.TimeUnit

@HiltWorker
class DeadlineReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: TaskRepositoryCombinedImpl
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val CHANNEL_ID = "deadline_reminders"
        private const val CHANNEL_NAME = "Deadline Reminders"

        fun start(context: Context) {
            val work = PeriodicWorkRequestBuilder<DeadlineReminderWorker>(
                15, TimeUnit.MINUTES
            ).build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    "deadline_reminder_worker",
                    ExistingPeriodicWorkPolicy.KEEP,
                    work
                )
        }
    }

    override suspend fun doWork(): Result {
        AppLog.d("DeadlineReminder", "Worker started at ${Instant.now()}")
        createNotificationChannel()

        val tasks = repository.getAllTasks()
        AppLog.d("DeadlineReminder", "Tasks loaded: ${tasks.size}")

        val now = Instant.now()
        val activeTasks = tasks.filter {
            !it.isDeleted && it.status != Status.COMPLETED && it.status != Status.CANCELLED && it.dueDate != null
        }

        AppLog.d("DeadlineReminder", "Active tasks for notification: ${activeTasks.size}")

        activeTasks.forEach { task ->
            AppLog.d("DeadlineReminder", "Processing task: ${task.title}, dueDate=${task.dueDate}")

            val dueInstant = try {
                Instant.parse(task.dueDate)
            } catch (e: Exception) {
                AppLog.e("DeadlineReminder", "Failed to parse dueDate: ${task.dueDate}", e)
                null
            }

            dueInstant?.let { due ->
                val diffMillis = due.toEpochMilli() - now.toEpochMilli()
                val message = when {
                    diffMillis < 0 -> "Просрочено!"
                    diffMillis <= TimeUnit.HOURS.toMillis(1) -> "Дедлайн через 1 час!"
                    diffMillis <= TimeUnit.DAYS.toMillis(1) -> "Дедлайн сегодня"
                    else -> null
                }
                message?.let {
                    AppLog.d("DeadlineReminder", "Showing notification: $it")
                    showNotification(task, it)
                }
            }
        }

        return Result.success()
    }

    class SnoozeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val taskUuid = intent?.getStringExtra("snooze_task_uuid") ?: return
            val minutes = intent.getIntExtra("snooze_minutes", 60)

            val work = OneTimeWorkRequestBuilder<DeadlineReminderWorker>()
                .setInitialDelay(minutes.toLong(), TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(context).enqueue(work)
            AppLog.d("SnoozeReceiver", "Snooze for $taskUuid for $minutes minutes")
        }
    }


    private fun showNotification(task: Task, text: String) {
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val colorInt = when (task.priority) {
            Priority.HIGH -> 0xFFD32F2F.toInt()
            Priority.MEDIUM -> 0xFFFFB74D.toInt()
            Priority.LOW -> 0xFF90A4AE.toInt()
        }


        val openIntent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("navigate_to_task", true)
            putExtra("board_uuid", task.boardUuid)
            putExtra("task_uuid", task.uuid)
        }

        val openPending = PendingIntent.getActivity(
            applicationContext,
            task.uuid.hashCode(),
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java).apply {
            putExtra("snooze_task_uuid", task.uuid)
            putExtra("snooze_minutes", 60)
        }

        val snoozePending = PendingIntent.getBroadcast(
            applicationContext,
            ("snooze:" + task.uuid).hashCode(),
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val bigText = NotificationCompat.BigTextStyle().bigText(task.description ?: text)
        val notif = NotificationCompat.Builder(applicationContext, CHANNEL_ID /*or urgent*/)
            .setSmallIcon(com.example.sharkflow.R.drawable.logo)
            .setContentTitle(task.title)
            .setContentText(text)
            .setStyle(bigText)
            .setContentIntent(openPending)
//            .addAction(
//                com.example.sharkflow.R.drawable.ic_home_space_mode_switch,
//                "Отложить уведомление на 1ч",
//                snoozePending
//            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setColor(colorInt)
            .setColorized(true)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setGroup("deadline_group")
            .build()

        val notifyId = kotlin.math.abs(task.uuid.hashCode())
        manager.notify(notifyId, notif)
    }


    private fun createNotificationChannel() {
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val urgent = NotificationChannel(
            "deadline_urgent",
            "Срочные дедлайны",
            NotificationManager.IMPORTANCE_HIGH
        )
        urgent.description = "Срочные дедлайны"
        manager.createNotificationChannel(urgent)

        val normal =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        normal.description = "Напоминания о дедлайнах"
        manager.createNotificationChannel(normal)
    }

}
