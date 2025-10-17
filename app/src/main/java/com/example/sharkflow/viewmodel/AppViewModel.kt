package com.example.sharkflow.viewmodel

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.sharkflow.data.local.language.LanguageState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor() : ViewModel() {

    var isLanguageInitialized by mutableStateOf(false)
        private set

    fun initializeApp(context: Context) {
        LanguageState.init(context)
        isLanguageInitialized = true
    }
}
