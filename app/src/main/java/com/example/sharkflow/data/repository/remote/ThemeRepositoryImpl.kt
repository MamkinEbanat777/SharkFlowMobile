package com.example.sharkflow.data.repository.remote

import com.example.sharkflow.data.local.preference.ThemePreference
import com.example.sharkflow.domain.repository.ThemeRepository
import jakarta.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepositoryImpl @Inject constructor(
    private val themePreference: ThemePreference
) : ThemeRepository {
    override suspend fun isDarkTheme(): Boolean = themePreference.get()
    override fun setDarkTheme(isDark: Boolean) = themePreference.set(isDark)
}
