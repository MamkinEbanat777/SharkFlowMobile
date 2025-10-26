package com.example.sharkflow.data.local.preference

import android.content.Context
import androidx.core.content.edit
import java.util.Locale

object LanguagePreference {
    private const val PREFS_NAME = "language_prefs"
    private const val KEY_LANGUAGE = "app_language"

    fun get(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, Locale.getDefault().language) ?: ""
    }

    fun set(context: Context, language: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putString(KEY_LANGUAGE, language) }
    }
}

