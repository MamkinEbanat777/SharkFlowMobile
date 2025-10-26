package com.example.sharkflow

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.sharkflow.domain.repository.LanguageRepository
import com.example.sharkflow.utils.*
import com.example.sharkflow.worker.startTaskSyncWorker
import com.google.crypto.tink.aead.AeadConfig
import com.google.firebase.*
import com.google.firebase.analytics.analytics
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject

@HiltAndroidApp
class SharkFlowApp : Application(), Configuration.Provider {
    @Inject
    lateinit var languageRepository: LanguageRepository

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Firebase.analytics
        Lang.init(languageRepository)
        startTaskSyncWorker(this)

        try {
            AeadConfig.register()
        } catch (e: Exception) {
            AppLog.w("AeadConfig.register(): ${e.message}")
        }

        try {
            SecureCrypto.init(this)
        } catch (e: Exception) {
            AppLog.e("SharkFlowApp: SecureCrypto.init failed", e)
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
