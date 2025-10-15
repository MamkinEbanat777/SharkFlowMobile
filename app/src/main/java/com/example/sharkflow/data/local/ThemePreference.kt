package com.example.sharkflow.data.local

import android.content.Context
import androidx.core.content.edit

object ThemePreference {
    private const val PREFS_NAME = "app_theme"
    private const val KEY_THEME = "isDarkTheme"

    fun get(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_THEME, false)
    }

    fun set(context: Context, isDarkTheme: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putBoolean(KEY_THEME, isDarkTheme) }
    }
}
