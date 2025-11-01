package com.example.sharkflow.worker.updateApp

import android.content.Context
import androidx.work.*
import com.example.sharkflow.BuildConfig
import com.example.sharkflow.core.system.fetchUpdateInfo

class UpdateCheckWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val manifestUrl = inputData.getString("manifestUrl")
            ?: return Result.failure()
        return try {
            val info = fetchUpdateInfo(manifestUrl)
            if (info != null && info.versionCode > BuildConfig.VERSION_CODE) {
                // здесь можно отправить уведомление пользователю (или сохранить флаг в prefs)
                // пример: sendNotification("Доступно обновление ${info.versionName}")
            }
            Result.success()
        } catch (t: Throwable) {
            Result.retry()
        }
    }
}
