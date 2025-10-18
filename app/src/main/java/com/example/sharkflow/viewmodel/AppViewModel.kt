package com.example.sharkflow.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.sharkflow.domain.repository.LanguageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val languageRepository: LanguageRepository
) : ViewModel() {

    var isLanguageInitialized by mutableStateOf(false)
        private set

    fun initializeApp() {
        languageRepository.initialize()
        isLanguageInitialized = true
    }

}
