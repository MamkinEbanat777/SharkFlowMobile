package com.example.sharkflow.data.local


import android.content.*
import androidx.core.content.*

private const val PREFS_NAME = "app_theme"
private const val KEY_THEME = "isDarkTheme"

fun getThemePreference(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean(KEY_THEME, false)
}

fun setThemePreference(context: Context, isDarkTheme: Boolean) {
    val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putBoolean(KEY_THEME, isDarkTheme)
    }
}
