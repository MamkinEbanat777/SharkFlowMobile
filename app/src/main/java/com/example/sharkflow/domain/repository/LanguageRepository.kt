package com.example.sharkflow.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface LanguageRepository {
    val currentLanguageFlow: StateFlow<String>
    suspend fun getCurrentLanguage(): String
    fun initialize()
    fun updateLanguage(langCode: String)
}