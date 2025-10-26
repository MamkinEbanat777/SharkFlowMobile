package com.example.sharkflow

import android.app.Application
import com.example.sharkflow.domain.repository.LanguageRepository
import com.example.sharkflow.utils.*
import com.google.crypto.tink.aead.AeadConfig
import com.google.firebase.*
import com.google.firebase.analytics.analytics
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject

@HiltAndroidApp
class SharkFlowApp : Application() {
    @Inject
    lateinit var languageRepository: LanguageRepository
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Firebase.analytics
        Lang.init(languageRepository)
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
}
