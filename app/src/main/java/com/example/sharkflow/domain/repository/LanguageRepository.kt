package com.example.sharkflow.domain.repository

import kotlinx.coroutines.flow.Flow

interface LanguageRepository {
    val currentLanguageFlow: Flow<String>
    suspend fun getCurrentLanguage(): String
    fun initialize()
    fun updateLanguage(langCode: String)
}

