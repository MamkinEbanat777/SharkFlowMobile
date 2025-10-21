package com.example.sharkflow.data.repository.remote

import android.content.Context
import com.example.sharkflow.data.local.LanguagePreference
import com.example.sharkflow.domain.repository.LanguageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import java.util.Locale

class LanguageRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : LanguageRepository {
    private val _languageFlow = MutableStateFlow("")
    override val currentLanguageFlow: StateFlow<String> = _languageFlow.asStateFlow()

    init {
        val saved = LanguagePreference.get(context)
        _languageFlow.value = saved.ifBlank { Locale.getDefault().language }
    }

    override suspend fun getCurrentLanguage(): String = _languageFlow.value

    override fun initialize() {
        val saved = LanguagePreference.get(context)
        _languageFlow.value = saved.ifBlank { Locale.getDefault().language }
    }

    override fun updateLanguage(langCode: String) {
        _languageFlow.value = langCode
        LanguagePreference.set(context, langCode)
    }
}
