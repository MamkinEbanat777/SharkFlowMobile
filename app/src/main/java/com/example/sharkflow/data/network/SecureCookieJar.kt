package com.example.sharkflow.data.network

import android.content.Context
import androidx.core.content.edit
import com.example.sharkflow.utils.*
import com.google.gson.Gson
import okhttp3.*

class SecureCookieJar(context: Context) : CookieJar {
    private val appContext = context.applicationContext
    private val prefsName = "secure_cookies_enc"
    private val prefs = appContext.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    private val gson = Gson()

    private val memoryStore = mutableMapOf<String, List<Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        memoryStore[url.host] = cookies
        try {
            val listAsStrings = cookies.map { it.toString() }
            val json = gson.toJson(listAsStrings)
            val cipherB64 = SecureCrypto.encryptToBase64(json)
            prefs.edit { putString(url.host, cipherB64) }
        } catch (e: Exception) {
            AppLog.e("saveFromResponse error: ${e.message}", e)
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        memoryStore[url.host]?.let { return it }

        val cipherB64 = prefs.getString(url.host, null) ?: return emptyList()
        return try {
            val json = SecureCrypto.decryptFromBase64(cipherB64)
            val arr = gson.fromJson(json, Array<String>::class.java)
            val cookies = arr.mapNotNull { Cookie.parse(url, it) }
            memoryStore[url.host] = cookies
            cookies
        } catch (e: Exception) {
            AppLog.e("loadForRequest error for ${url.host}: ${e.message}", e)
            emptyList()
        }
    }
}
