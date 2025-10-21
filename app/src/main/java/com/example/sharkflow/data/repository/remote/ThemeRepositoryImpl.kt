package com.example.sharkflow.data.repository.remote

import android.content.Context
import androidx.core.content.edit
import com.example.sharkflow.domain.repository.ThemeRepository
import jakarta.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepositoryImpl @Inject constructor(private val context: Context) : ThemeRepository {
    private val prefs by lazy { context.getSharedPreferences("app_theme", Context.MODE_PRIVATE) }
    override suspend fun isDarkTheme(): Boolean {
        return prefs.getBoolean("isDarkTheme", false)
    }

    override fun setDarkTheme(isDark: Boolean) {
        prefs.edit { putBoolean("isDarkTheme", isDark) }
    }

}