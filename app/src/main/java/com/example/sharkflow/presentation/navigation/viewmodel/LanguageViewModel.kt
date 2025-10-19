package com.example.sharkflow.presentation.navigation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.sharkflow.domain.repository.LanguageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val languageRepository: LanguageRepository
) : ViewModel() {
    val currentLanguageFlow = languageRepository.currentLanguageFlow

    fun initialize() {
        languageRepository.initialize()
    }

    fun updateLanguage(langCode: String) {
        languageRepository.updateLanguage(langCode)
    }
}
