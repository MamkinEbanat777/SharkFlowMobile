package com.example.sharkflow.domain.repository

interface ThemeRepository {
    suspend fun isDarkTheme(): Boolean
    fun setDarkTheme(isDark: Boolean)
}