package com.example.sharkflow.data.local.preference

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class ThemePreference @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("app_theme", Context.MODE_PRIVATE)

    fun get(): Boolean = prefs.getBoolean("isDarkTheme", false)

    fun set(isDarkTheme: Boolean) {
        prefs.edit { putBoolean("isDarkTheme", isDarkTheme) }
    }
}
