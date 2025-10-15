package com.example.sharkflow.data.local.language

import android.content.Context
import androidx.compose.runtime.*
import java.util.Locale

object LanguageState {
    var currentLanguage: String by mutableStateOf("")

    fun init(context: Context) {
        val saved = LanguagePreference.get(context)
        currentLanguage = saved.ifBlank { Locale.getDefault().language }
    }
}
