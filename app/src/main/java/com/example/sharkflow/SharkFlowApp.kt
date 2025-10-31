package com.example.sharkflow

import android.app.Application
import android.net.*
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.sharkflow.core.common.*
import com.example.sharkflow.core.system.AppLog
import com.example.sharkflow.domain.repository.LanguageRepository
import com.example.sharkflow.worker.startFullSyncChain
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

        observeNetworkChanges()
        startFullSyncChain(this)
//        startDeadlineReminderWorker(this)

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

    private fun observeNetworkChanges() {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()

        cm.registerNetworkCallback(request, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                startFullSyncChain(this@SharkFlowApp)
            }
        })
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
