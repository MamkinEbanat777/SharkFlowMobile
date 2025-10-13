package com.example.sharkflow

import android.app.*
import android.util.*
import com.example.sharkflow.utils.*
import com.google.crypto.tink.*
import com.google.crypto.tink.aead.*
import com.google.crypto.tink.integration.android.*
import dagger.hilt.android.*

@HiltAndroidApp
class SharkFlowApp : Application() {

    override fun onCreate() {
        super.onCreate()

        try {
            AeadConfig.register()
        } catch (e: Exception) {
            Log.w("SharkFlowApp", "AeadConfig.register(): ${e.message}")
        }

        try {
            val keysetHandle = AndroidKeysetManager.Builder()
                .withSharedPref(this, "tink_keyset", "tink_key_prefs")
                .withKeyTemplate(AeadKeyTemplates.AES256_GCM)
                .withMasterKeyUri("android-keystore://tink_master_key")
                .build()
                .keysetHandle

            val aead: Aead = keysetHandle.getPrimitive(Aead::class.java)

            SecureCrypto.init(aead)

        } catch (e: Exception) {
            Log.e("SharkFlowApp", "Ошибка инициализации крипты: ${e.message}")
        }
    }
}
