package com.example.sharkflow.utils

import android.util.*
import com.google.crypto.tink.*
import java.nio.charset.*

object SecureCrypto {
    private lateinit var aead: Aead
    private var initialized = false

    fun init(aeadInstance: Aead) {
        aead = aeadInstance
        initialized = true
    }

    private fun ensureInit() {
        if (!initialized) throw IllegalStateException("SecureCrypto is not initialized")
    }

    fun encrypt(plainText: String, associatedData: ByteArray? = null): ByteArray {
        ensureInit()
        return aead.encrypt(
            plainText.toByteArray(StandardCharsets.UTF_8),
            associatedData ?: ByteArray(0)
        )
    }

    fun decrypt(cipherText: ByteArray, associatedData: ByteArray? = null): String {
        ensureInit()
        val pt = aead.decrypt(cipherText, associatedData ?: ByteArray(0))
        return String(pt, StandardCharsets.UTF_8)
    }

    fun encryptToBase64(plainText: String, associatedData: ByteArray? = null): String {
        val cipher = encrypt(plainText, associatedData)
        return Base64.encodeToString(cipher, Base64.NO_WRAP)
    }

    fun decryptFromBase64(base64: String, associatedData: ByteArray? = null): String {
        val cipher = Base64.decode(base64, Base64.NO_WRAP)
        return decrypt(cipher, associatedData)
    }
}
